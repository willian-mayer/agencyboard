package com.agencyboard.agencyboard.campaign;

import com.agencyboard.agencyboard.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByClient(Client client);
}