package com.linkedIn.file_service.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.linkedIn.file_service.exception.ApiException;
import com.linkedIn.file_service.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private final String UPLOAD_FILE_ERROR = "Error while uploading file";
    private final String SECURE_URL_KEY = "secure_url";

    private final Cloudinary cloudinary;

    public FileServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        try {
            byte[] fileBytes = multipartFile.getBytes();

            Map result = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());

            return String.valueOf(result.get(SECURE_URL_KEY));
        }
        catch (IOException | RuntimeException exception) {
            exception.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public void deleteFileById(String fileId) {
        try {
            cloudinary.api()
                    .deleteResources(Collections.singletonList(fileId),
                            ObjectUtils.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "File with id " + fileId + " was not found");
        }
    }

    @Override
    public void deleteBatchFiles(String[] fileIds) {
        if (fileIds == null || fileIds.length == 0) throw new ApiException(HttpStatus.BAD_REQUEST, "fileIds not found or empty");

        try {
            cloudinary.api()
                    .deleteResources(Arrays.asList(fileIds),
                            ObjectUtils.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error batch deleting files");
        }
    }
}
