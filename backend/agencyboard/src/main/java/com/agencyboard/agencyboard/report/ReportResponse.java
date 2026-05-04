package com.agencyboard.agencyboard.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ReportResponse {
    private Long id;
    private String title;
    private String content;
    private Long clientId;
    private LocalDateTime createdAt;

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .title(report.getTitle())
                .content(report.getContent())
                .clientId(report.getClient().getId())
                .createdAt(report.getCreatedAt())
                .build();
    }
}