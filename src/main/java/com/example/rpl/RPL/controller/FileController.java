package com.example.rpl.RPL.controller;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FileController {

    private final FileRepository fileRepository;

    @Autowired
    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @GetMapping(value = "/api/files/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId,
        HttpServletRequest request) {
        log.error("FILE ID: {}", fileId);

        RPLFile f = fileRepository.findById(fileId).orElseThrow(
            () -> new NotFoundException("File not found",
                "file_not_found"));

        Resource resource = new ByteArrayResource(f.getData());

        String contentType = null;
        try {
            contentType = request.getServletContext()
                .getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/gzip";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + f.getFileName() + "\"")
            .body(resource);
    }


    @GetMapping(value = "/api/getExtractedFile/{fileId}")
    public ResponseEntity<Map<String, String>> getExtractedFile(@PathVariable Long fileId,
        HttpServletRequest request) throws IOException {
        log.error("FILE ID: {}", fileId);

        Map<String, String> r = extractFile(fileId);

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @GetMapping(value = "/api/getFileForStudent/{fileId}")
    public ResponseEntity<Map<String, String>> getFileForStudent(@PathVariable Long fileId,
        HttpServletRequest request) throws IOException {
        log.error("FILE ID: {}", fileId);

        Map<String, String> r = extractFile(fileId);
        if (!r.containsKey("files_metadata")) {
            return new ResponseEntity<>(r, HttpStatus.OK);
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
        return new ResponseEntity<>(filteredFiles, HttpStatus.OK);
    }

    @GetMapping(value = "/api/getExtractedFiles/{filesIds}")
    public ResponseEntity<List<Map<String, String>>> getExtractedFiles(
        @PathVariable Long[] filesIds,
        HttpServletRequest request) {

        List<Map<String, String>> files = new ArrayList<>();

        for (Long fileId : filesIds) {
            try {
                files.add(extractFile(fileId));
            } catch (IOException e) {
                log.error("Error trying to extract file {}. Exception: {}", fileId, e.getMessage());
            }
        }

        return new ResponseEntity<>(files, HttpStatus.OK);
    }


    private Map<String, String> extractFile(Long fileId) throws IOException {
        RPLFile f = fileRepository.findById(fileId).orElseThrow(
            () -> new NotFoundException("File not found",
                "file_not_found"));

        Resource resource = new ByteArrayResource(f.getData());

        return TarUtils.extractTarGZ(resource.getInputStream());
    }
}