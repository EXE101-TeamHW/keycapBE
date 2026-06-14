package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.report.RevenueReportItem;
import com.keycap.keycapdesign.dto.report.StaffPerformanceItem;
import com.keycap.keycapdesign.dto.report.TrendItem;
import com.keycap.keycapdesign.dto.report.DashboardSummaryResponse;
import com.keycap.keycapdesign.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/revenue")
    public ApiResponse<List<RevenueReportItem>> revenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String groupBy) {
        return ApiResponse.success(reportService.revenue(from, to, groupBy));
    }

    @GetMapping("/staff-performance")
    public ApiResponse<List<StaffPerformanceItem>> staffPerformance() {
        return ApiResponse.success(reportService.staffPerformance());
    }

    @GetMapping("/trends")
    public ApiResponse<List<TrendItem>> trends() {
        return ApiResponse.success(reportService.trends());
    }

    @GetMapping("/dashboard-summary")
    public ApiResponse<DashboardSummaryResponse> dashboardSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(reportService.dashboardSummary(from, to));
    }
}
