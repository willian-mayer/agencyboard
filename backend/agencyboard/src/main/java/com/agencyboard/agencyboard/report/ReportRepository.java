package com.agencyboard.agencyboard.report;

import com.agencyboard.agencyboard.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByClient(Client client);
}