package com.agencyboard.agencyboard.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private String industry;
    private String notes;
    private LocalDateTime createdAt;

    public static ClientResponse from(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .company(client.getCompany())
                .industry(client.getIndustry())
                .notes(client.getNotes())
                .createdAt(client.getCreatedAt())
                .build();
    }
}