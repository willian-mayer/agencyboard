package com.agencyboard.agencyboard.campaign;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping("/api/clients/{clientId}/campaigns")
    public ResponseEntity<List<CampaignResponse>> getCampaigns(@PathVariable Long clientId) {
        return ResponseEntity.ok(campaignService.getCampaigns(clientId));
    }

    @PostMapping("/api/clients/{clientId}/campaigns")
    public ResponseEntity<CampaignResponse> createCampaign(
            @PathVariable Long clientId,
            @Valid @RequestBody CampaignRequest request) {
        return ResponseEntity.ok(campaignService.createCampaign(clientId, request));
    }

    @PatchMapping("/api/campaigns/{id}/status")
    public ResponseEntity<CampaignResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam CampaignStatus status) {
        return ResponseEntity.ok(campaignService.updateStatus(id, status));
    }
}