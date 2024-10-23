package com.linkedIn.file_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile multipartFile);
    void deleteFileById(String fileId);
    void deleteBatchFiles(String[] fileIds);
}
