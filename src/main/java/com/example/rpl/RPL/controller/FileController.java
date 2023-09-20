package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.RPLFile;
import com.example.rpl.RPL.repository.FileRepository;
import com.example.rpl.RPL.service.RplFilesService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
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
    private RplFilesService rplFilesService;

    @Autowired
    public FileController(FileRepository fileRepository,
        RplFilesService rplFilesService) {
        this.fileRepository = fileRepository;
        this.rplFilesService = rplFilesService;
    }

    /**
     * Returns the file "as is" compressed as a tar.gz
     */
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

    /**
     * Extracts the tar.gz and returns the files as a JSON object where the key is the filename and
     * the value is the content.
     */
    @GetMapping(value = "/api/getExtractedFile/{fileId}")
    public ResponseEntity<Map<String, String>> getExtractedFile(@PathVariable Long fileId,
        HttpServletRequest request) throws IOException {
        log.error("FILE ID: {}", fileId);

        Map<String, String> r = rplFilesService.extractFile(fileId);

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    /**
     * Extracts the tar.gz and returns the files as a JSON object where the key is the filename and
     * the value is the content. Only returns files with metadata "display" field "READ" or
     * "READ_WRITE". Doesn't return files with metadata {display: "hidden"}
     */
    @GetMapping(value = "/api/getFileForStudent/{fileId}")
    public ResponseEntity<Map<String, String>> getFileForStudent(@PathVariable Long fileId,
        HttpServletRequest request) throws IOException {
        log.error("FILE ID: {}", fileId);

        Map<String, String> filteredFiles = rplFilesService.extractFileForStudent(fileId);

        return new ResponseEntity<>(filteredFiles, HttpStatus.OK);
    }

    /**
     * Same as @getExtractedFile but returns a list with all the extracted fileIds in the query
     * list.
     */
    @GetMapping(value = "/api/getExtractedFiles/{filesIds}")
    public ResponseEntity<List<Map<String, String>>> getExtractedFiles(
        @PathVariable Long[] filesIds,
        HttpServletRequest request) {

        List<Map<String, String>> files = new ArrayList<>();

        for (Long fileId : filesIds) {
            try {
                files.add(rplFilesService.extractFile(fileId));
            } catch (IOException e) {
                log.error("Error trying to extract file {}. Exception: {}", fileId, e.getMessage());
            }
        }

        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    /**
     * Same as @getFileForStudent but returns a list with all the extracted fileIds in the query
     * list.
     */
    @GetMapping(value = "/api/getFilesForStudent/{filesIds}")
    public ResponseEntity<List<Map<String, String>>> getExtractedFilesForStudent(
        @PathVariable Long[] filesIds,
        HttpServletRequest request) {

        List<Map<String, String>> files = new ArrayList<>();

        for (Long fileId : filesIds) {
            try {
                files.add(rplFilesService.extractFileForStudent(fileId));
            } catch (IOException e) {
                log.error("Error trying to extract file {}. Exception: {}", fileId, e.getMessage());
            }
        }

        return new ResponseEntity<>(files, HttpStatus.OK);
    }
}
