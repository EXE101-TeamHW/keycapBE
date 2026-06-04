package com.keycap.keycapdesign.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ExternalServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, Object> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto"
            ));
        } catch (IOException e) {
            throw new BadRequestException("Upload failed");
        } catch (RuntimeException e) {
            throw new ExternalServiceException(toCloudinaryErrorMessage(e), e);
        }
    }

    private String toCloudinaryErrorMessage(RuntimeException e) {
        String message = e.getMessage();
        if (message != null && message.toLowerCase().contains("invalid cloud_name")) {
            return "Invalid Cloudinary cloud name. Check cloudinary.cloud-name in backend configuration.";
        }
        return "Cloudinary upload failed. Check backend Cloudinary configuration.";
    }
}
