package com.agencyboard.agencyboard.dashboard;

import com.agencyboard.agencyboard.campaign.CampaignRepository;
import com.agencyboard.agencyboard.campaign.CampaignStatus;
import com.agencyboard.agencyboard.client.Client;
import com.agencyboard.agencyboard.client.ClientRepository;
import com.agencyboard.agencyboard.report.ReportRepository;
import com.agencyboard.agencyboard.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClientRepository clientRepository;
    private final CampaignRepository campaignRepository;
    private final ReportRepository reportRepository;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public DashboardResponse getStats() {
        User currentUser = getCurrentUser();
        List<Client> clients = clientRepository.findByCreatedBy(currentUser);

        long totalCampaigns = 0;
        long activeCampaigns = 0;
        long totalReports = 0;
        BigDecimal totalBudget = BigDecimal.ZERO;
        BigDecimal totalSpent = BigDecimal.ZERO;

        for (Client client : clients) {
            var campaigns = campaignRepository.findByClient(client);
            totalCampaigns += campaigns.size();
            activeCampaigns += campaigns.stream()
                    .filter(c -> c.getStatus() == CampaignStatus.ACTIVE)
                    .count();
            totalBudget = totalBudget.add(campaigns.stream()
                    .filter(c -> c.getBudget() != null)
                    .map(c -> c.getBudget())
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            totalSpent = totalSpent.add(campaigns.stream()
                    .filter(c -> c.getSpent() != null)
                    .map(c -> c.getSpent())
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            totalReports += reportRepository.findByClient(client).size();
        }

        return DashboardResponse.builder()
                .totalClients(clients.size())
                .totalCampaigns(totalCampaigns)
                .activeCampaigns(activeCampaigns)
                .totalReports(totalReports)
                .totalBudget(totalBudget)
                .totalSpent(totalSpent)
                .build();
    }
}