package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.custom.CustomRequestCreateRequest;
import com.keycap.keycapdesign.dto.custom.CustomRequestResponse;
import com.keycap.keycapdesign.service.CustomRequestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/custom-requests")
public class CustomRequestController {
    private final CustomRequestService customRequestService;

    public CustomRequestController(CustomRequestService customRequestService) {
        this.customRequestService = customRequestService;
    }

    @PostMapping
    public ApiResponse<CustomRequestResponse> create(@Valid @RequestBody CustomRequestCreateRequest request) {
        return ApiResponse.success(customRequestService.create(request));
    }

    @GetMapping
    public ApiResponse<List<CustomRequestResponse>> list(@RequestParam Long userId) {
        return ApiResponse.success(customRequestService.listByUser(userId));
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomRequestResponse> get(@PathVariable Long id) {
        return ApiResponse.success(customRequestService.getById(id));
    }
}

