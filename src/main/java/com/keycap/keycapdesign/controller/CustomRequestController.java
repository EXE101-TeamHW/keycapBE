package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.custom.CustomRequestCreateRequest;
import com.keycap.keycapdesign.dto.custom.CustomRequestResponse;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.CustomRequestService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.exception.UnauthorizedException;

import java.util.List;

@RestController
@RequestMapping("/api/custom-requests")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomRequestController {
    private final CustomRequestService customRequestService;
    private final CurrentUserService currentUserService;

    public CustomRequestController(CustomRequestService customRequestService, CurrentUserService currentUserService) {
        this.customRequestService = customRequestService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ApiResponse<CustomRequestResponse> create(@Valid @RequestBody CustomRequestCreateRequest request) {
        request.setUserId(currentUserService.getCurrentUserId());
        return ApiResponse.success(customRequestService.create(request));
    }

    @GetMapping
    public ApiResponse<List<CustomRequestResponse>> list() {
        return ApiResponse.success(customRequestService.listByUser(currentUserService.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomRequestResponse> get(@PathVariable Long id) {
        CustomRequestResponse response = customRequestService.getById(id);
        if (!currentUserService.getCurrentUserId().equals(response.getUserId())) {
            throw new UnauthorizedException("Access denied");
        }
        return ApiResponse.success(response);
    }
}
