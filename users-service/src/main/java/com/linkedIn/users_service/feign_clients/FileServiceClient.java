package com.linkedIn.users_service.feign_clients;

import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name="file-service", url="${file.service.url}")
public interface FileServiceClient {

    @PostMapping(value = "/api/files", consumes = "multipart/form-data")
    @Headers("Content-Type: multipart/form-data")
    String uploadFile(@Param("file") MultipartFile file);
}
