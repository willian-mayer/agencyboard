package com.agencyboard.agencyboard.report;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients/{clientId}/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<ReportResponse> generateReport(@PathVariable Long clientId) {
        return ResponseEntity.ok(reportService.generateReport(clientId));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getReports(@PathVariable Long clientId) {
        return ResponseEntity.ok(reportService.getReports(clientId));
    }
}