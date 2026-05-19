package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.upload.UploadResponse;
import com.keycap.keycapdesign.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {
    private final CloudinaryService cloudinaryService;

    public UploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UploadResponse> upload(
            @Parameter(description = "File to upload", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(type = "string", format = "binary")))
            @RequestPart("file") MultipartFile file) {
        Map<String, Object> result = cloudinaryService.upload(file);
        String url = String.valueOf(result.get("secure_url"));
        String publicId = String.valueOf(result.get("public_id"));
        return ApiResponse.success(new UploadResponse(url, publicId));
    }

    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<UploadResponse>> uploadMany(
            @Parameter(description = "Files to upload", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(type = "string", format = "binary")))
            @RequestPart("files") List<MultipartFile> files) {
        List<UploadResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> result = cloudinaryService.upload(file);
            String url = String.valueOf(result.get("secure_url"));
            String publicId = String.valueOf(result.get("public_id"));
            responses.add(new UploadResponse(url, publicId));
        }
        return ApiResponse.success(responses);
    }


//    UploadController dùng để:
//    Upload file (ảnh) từ client lên Cloudinary, rồi trả về URL ảnh + publicId. FE dùng URL này để lưu vào các entity như sản phẩm, mockup, custom request…
//    Endpoint
//    POST /api/uploads
//    Input: form-data file
//    Output: { url, publicId }
}
