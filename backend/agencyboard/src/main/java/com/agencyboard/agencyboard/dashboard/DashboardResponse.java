package com.agencyboard.agencyboard.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class DashboardResponse {
    private long totalClients;
    private long totalCampaigns;
    private long activeCampaigns;
    private long totalReports;
    private BigDecimal totalBudget;
    private BigDecimal totalSpent;
}