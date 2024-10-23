package com.linkedIn.file_service.controller;

import com.linkedIn.file_service.dto.BatchFileDeleteDto;
import com.linkedIn.file_service.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = this.fileService.uploadFile(file);

        return ResponseEntity.ok(url);
    }

    @PostMapping("/batch-delete")
    public void batchDeleteFiles(@RequestBody BatchFileDeleteDto batchFileDeleteDto) {
        this.fileService.deleteBatchFiles(batchFileDeleteDto.getFileIds());
    }

    @DeleteMapping("/{id}")
    public void deleteFileById(@PathVariable("id") String id) {
        this.fileService.deleteFileById(id);
    }
}
