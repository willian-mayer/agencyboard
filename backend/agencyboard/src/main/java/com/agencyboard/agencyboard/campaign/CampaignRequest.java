package com.agencyboard.agencyboard.campaign;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CampaignRequest {

    @NotBlank
    private String name;

    private String channel;
    private String objective;
    private BigDecimal budget;
}