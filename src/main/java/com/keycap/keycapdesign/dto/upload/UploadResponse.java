package com.keycap.keycapdesign.dto.upload;

public class UploadResponse {
    private String url;
    private String publicId;

    public UploadResponse(String url, String publicId) {
        this.url = url;
        this.publicId = publicId;
    }

    public String getUrl() {
        return url;
    }

    public String getPublicId() {
        return publicId;
    }
}

