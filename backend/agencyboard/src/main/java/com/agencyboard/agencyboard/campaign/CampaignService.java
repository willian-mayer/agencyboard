package com.agencyboard.agencyboard.campaign;

import com.agencyboard.agencyboard.client.Client;
import com.agencyboard.agencyboard.client.ClientRepository;
import com.agencyboard.agencyboard.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final ClientRepository clientRepository;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    private Client getClientForCurrentUser(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (!client.getCreatedBy().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Access denied");
        }

        return client;
    }

    public List<CampaignResponse> getCampaigns(Long clientId) {
        Client client = getClientForCurrentUser(clientId);
        return campaignRepository.findByClient(client)
                .stream()
                .map(CampaignResponse::from)
                .toList();
    }

    public CampaignResponse createCampaign(Long clientId, CampaignRequest request) {
        Client client = getClientForCurrentUser(clientId);

        Campaign campaign = Campaign.builder()
                .name(request.getName())
                .channel(request.getChannel())
                .objective(request.getObjective())
                .budget(request.getBudget())
                .client(client)
                .build();

        return CampaignResponse.from(campaignRepository.save(campaign));
    }

    public CampaignResponse updateStatus(Long id, CampaignStatus status) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        if (!campaign.getClient().getCreatedBy().getId().equals(getCurrentUser().getId())) {
            throw new RuntimeException("Access denied");
        }

        campaign.setStatus(status);
        return CampaignResponse.from(campaignRepository.save(campaign));
    }
}