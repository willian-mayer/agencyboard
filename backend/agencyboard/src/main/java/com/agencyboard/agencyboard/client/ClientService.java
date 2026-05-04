package com.agencyboard.agencyboard.client;

import com.agencyboard.agencyboard.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public List<ClientResponse> getClients() {
        return clientRepository.findByCreatedBy(getCurrentUser())
                .stream()
                .map(ClientResponse::from)
                .toList();
    }

    public ClientResponse createClient(ClientRequest request) {
        Client client = Client.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .company(request.getCompany())
                .industry(request.getIndustry())
                .notes(request.getNotes())
                .createdBy(getCurrentUser())
                .build();

        return ClientResponse.from(clientRepository.save(client));
    }

    public ClientResponse getClient(Long id) {
        User currentUser = getCurrentUser();
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (!client.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        return ClientResponse.from(client);
    }
}