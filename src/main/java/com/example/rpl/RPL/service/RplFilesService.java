package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.FileMetadata;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.utils.TarUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class RplFilesService {

    private final FileRepository fileRepository;

    @Autowired
    public RplFilesService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Decompresses the tar.gz file.
     *
     * @return a Map where key -> fileName and value -> fileContent.
     */
    public Map<String, String> extractFile(Long fileId) throws IOException {
        RPLFile f = fileRepository.findById(fileId).orElseThrow(
            () -> new NotFoundException("File not found",
                "file_not_found"));

        Resource resource = new ByteArrayResource(f.getData());

        return TarUtils.extractTarGZ(resource.getInputStream());
    }

    /**
     * The resulting submission package will be determined by: 1.- The student's files. 2.- The
     * activity's files with {display: "read"} metadata overwriting the student's files (in case
     * they managed to modify them). 3.- The activity's files with {display: "hidden"} metadata
     * (which are never sent to a student)
     *
     * @param submissionFiles Files submitted by the student
     * @param activityStartingFiles Files that are the skeleton of the activity (should contain an
     * extra "files_metada" JSON file)
     * @return An array of "byte" representing the subimssion's compressed files in a tar.gz.
     */
    byte[] addAndOverwriteWithActivityFiles(MultipartFile[] submissionFiles,
        RPLFile activityStartingFiles) throws IOException {

        Map<String, String> activityFiles = extractFile(activityStartingFiles.getId());

        if (!activityFiles.containsKey("files_metadata")) {
            // comprimir y devolver to do
            return TarUtils.compressToTarGz(submissionFiles);
        }

        Map<String, String> resultingSubmission = new HashMap<>();

        JsonNode root = new ObjectMapper().readTree(activityFiles.get("files_metadata"));
        ObjectNode metadataObjectNode = (ObjectNode) root;

        for (int i = 0; i < submissionFiles.length; i++) {
            MultipartFile file = submissionFiles[i];
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
//            System.err.println("Processing file " + fileName);

            if (metadataObjectNode.has(fileName)) {
                JsonNode fileMetadata = metadataObjectNode.get(fileName);
                FileMetadata metadata = new ObjectMapper()
                    .treeToValue(fileMetadata, FileMetadata.class);
                if (metadata.getDisplay().equals("read")) {
                    // If file was read only, overwrite with teacher's version of the file
                    resultingSubmission.put(fileName, activityFiles.get(fileName));
                } else {
                    resultingSubmission.put(fileName,
                        new String(bytes, 0, bytes.length, StandardCharsets.UTF_8).strip());
                }
            } else {
                resultingSubmission.put(fileName,
                    new String(bytes, 0, bytes.length, StandardCharsets.UTF_8).strip());
            }
        }

        // Add hidden files to the student
        activityFiles.forEach((fileName, content) -> {
            log.error("Processing activity file {}", fileName);
            if (!metadataObjectNode.has(fileName)) {
                return;
            }
            JsonNode fileMetadata = metadataObjectNode.get(fileName);
            FileMetadata metadata = null;
            try {
                metadata = new ObjectMapper().treeToValue(fileMetadata, FileMetadata.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (metadata.getDisplay().equals("hidden")) {
                resultingSubmission.put(fileName, activityFiles.get(fileName));
            }
        });
        resultingSubmission.put("files_metadata", activityFiles.get("files_metadata"));

        return TarUtils.compressToTarGz(resultingSubmission);
    }

    /**
     * Extracts the tar.gz and returns the files as a Map where the key is the filename and the
     * value is the file content. Only returns files with metadata {"display": "read"} or
     * {"display": "read_write"}. Doesn't return files with metadata {display: "hidden"}
     */
    public Map<String, String> extractFileForStudent(Long fileId) throws IOException {
        Map<String, String> r = extractFile(fileId);
        if (!r.containsKey("files_metadata")) {
            return r;
        }
        Map<String, String> filteredFiles = new HashMap<>();

        JsonNode root = new ObjectMapper().readTree(r.get("files_metadata"));
        ObjectNode objectNode = (ObjectNode) root;
        r.forEach((filename, fileContent) -> {
            if (!objectNode.has(filename)) {
                filteredFiles.put(filename, fileContent);
            } else {
                JsonNode fileMetadata = objectNode.get(filename);
                FileMetadata metadata = null;
                try {
                    metadata = new ObjectMapper().treeToValue(fileMetadata, FileMetadata.class);
                } catch (JsonProcessingException e) {
                    log.warn("File metadata bad formatted {} {}", filename, fileContent);
                    return;
                }
                if (!metadata.getDisplay().equals("hidden")) {
                    filteredFiles.put(filename, fileContent);
                }
            }
        });
        filteredFiles.put("files_metadata", r.get("files_metadata"));
        return filteredFiles;
    }
}
