package com.linkedin.post_service.feign_clients.impl;

import com.linkedin.post_service.feign_clients.FileServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceClientImpl {

    @Autowired
    private FileServiceClient fileUploadClient;

    public String uploadFile(MultipartFile file) {
        return fileUploadClient.uploadFile(file);
    }

    public void deleteFileById(String id) { fileUploadClient.deleteFileById(id); }
}
