package com.linkedIn.company_job_service.feign_clients;

import com.linkedIn.company_job_service.dto.BatchFileDeleteDto;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name="file-service", url="http://localhost:8084")
public interface FileServiceClient {

    @PostMapping(value = "/api/files", consumes = "multipart/form-data")
    @Headers("Content-Type: multipart/form-data")
    String uploadFile(@Param("file") MultipartFile file);

    @PostMapping("/api/files/batch-delete")
    void batchDeleteFiles(@RequestBody BatchFileDeleteDto batchFileDeleteDto);

    @DeleteMapping(value = "/api/files/{id}")
    void deleteFileById(@PathVariable("id") String id);
}
