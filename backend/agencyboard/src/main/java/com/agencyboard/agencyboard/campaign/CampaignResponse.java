package com.agencyboard.agencyboard.campaign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CampaignResponse {
    private Long id;
    private String name;
    private String channel;
    private String objective;
    private CampaignStatus status;
    private BigDecimal budget;
    private BigDecimal spent;
    private Long clientId;
    private LocalDateTime createdAt;

    public static CampaignResponse from(Campaign campaign) {
        return CampaignResponse.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .channel(campaign.getChannel())
                .objective(campaign.getObjective())
                .status(campaign.getStatus())
                .budget(campaign.getBudget())
                .spent(campaign.getSpent())
                .clientId(campaign.getClient().getId())
                .createdAt(campaign.getCreatedAt())
                .build();
    }
}