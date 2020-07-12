package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.FileMetadata;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.repository.ActivityRepository;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.repository.SubmissionRepository;
import com.example.rpl.RPL.repository.TestRunRepository;
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

    private final TestService testService;

    private final ActivityRepository activityRepository;
    private final SubmissionRepository submissionRepository;
    private final FileRepository fileRepository;
    private final TestRunRepository testRunRepository;

    @Autowired
    public RplFilesService(TestService testService,
        ActivityRepository activityRepository,
        SubmissionRepository submissionRepository,
        FileRepository fileRepository,
        TestRunRepository testRunRepository) {
        this.testService = testService;
        this.activityRepository = activityRepository;
        this.submissionRepository = submissionRepository;
        this.fileRepository = fileRepository;
        this.testRunRepository = testRunRepository;
    }


    public byte[] addAndOverwriteWithActivityFiles(MultipartFile[] submissionFiles,
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
            System.err.println("Processing file " + fileName);

            if (metadataObjectNode.has(fileName)) {
                JsonNode fileMetadata = metadataObjectNode.get(fileName);
                FileMetadata metadata = new ObjectMapper()
                    .treeToValue(fileMetadata, FileMetadata.class);
                if (!metadata.getDisplay().equals("read")) {
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

    private Map<String, String> extractFile(Long fileId) throws IOException {
        RPLFile f = fileRepository.findById(fileId).orElseThrow(
            () -> new NotFoundException("File not found",
                "file_not_found"));

        Resource resource = new ByteArrayResource(f.getData());

        return TarUtils.extractTarGZ(resource.getInputStream());
    }

//    private MultipartFile getNewFile(String fileName, byte[] content, MultipartFile currentFile){
//        return new MultipartFile() {
//            @Override
//            public String getName() {
//                return currentFile.getName();
//            }
//
//            @Override
//            public String getOriginalFilename() {
//                return fileName;
//            }
//
//            @Override
//            public String getContentType() {
//                return currentFile.getContentType();
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return currentFile.isEmpty();
//            }
//
//            @Override
//            public long getSize() {
//                return currentFile.getSize();
//            }
//
//            @Override
//            public byte[] getBytes() throws IOException {
//                return content;
//            }
//
//            @Override
//            public InputStream getInputStream() throws IOException {
//                return currentFile.getInputStream();
//            }
//
//            @Override
//            public void transferTo(File file) throws IOException, IllegalStateException {
//
//            }
//        };
//    }
}
