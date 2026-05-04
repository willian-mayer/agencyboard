package com.agencyboard.agencyboard.report;

import com.agencyboard.agencyboard.campaign.Campaign;
import com.agencyboard.agencyboard.campaign.CampaignRepository;
import com.agencyboard.agencyboard.client.Client;
import com.agencyboard.agencyboard.client.ClientRepository;
import com.agencyboard.agencyboard.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ClientRepository clientRepository;
    private final CampaignRepository campaignRepository;

    @Value("${openai.api-key}")
    private String openAiApiKey;

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

    public ReportResponse generateReport(Long clientId) {
        Client client = getClientForCurrentUser(clientId);
        List<Campaign> campaigns = campaignRepository.findByClient(client);

        String campaignsSummary = campaigns.stream()
                .map(c -> String.format(
                        "- %s | Channel: %s | Status: %s | Budget: $%s | Spent: $%s",
                        c.getName(), c.getChannel(), c.getStatus(), c.getBudget(), c.getSpent()
                ))
                .reduce("", (a, b) -> a + "\n" + b);

        String prompt = String.format("""
                You are a digital marketing analyst. Generate a concise executive report for the following client:
                
                Client: %s
                Industry: %s
                
                Campaigns:
                %s
                
                Write a professional 3-paragraph executive summary covering:
                1. Overall campaign performance overview
                2. Key insights and observations
                3. Recommendations for next steps
                
                Keep it concise and actionable.
                """, client.getName(), client.getIndustry(), campaignsSummary);

        String content = callOpenAi(prompt);

        Report report = Report.builder()
                .title("Executive Report — " + client.getName())
                .content(content)
                .client(client)
                .build();

        return ReportResponse.from(reportRepository.save(report));
    }

    public List<ReportResponse> getReports(Long clientId) {
        Client client = getClientForCurrentUser(clientId);
        return reportRepository.findByClient(client)
                .stream()
                .map(ReportResponse::from)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private String callOpenAi(String prompt) {
        RestClient restClient = RestClient.create();

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        Map<String, Object> response = restClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return (String) message.get("content");
    }
}