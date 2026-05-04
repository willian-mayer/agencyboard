package com.agencyboard.agencyboard.client;

import com.agencyboard.agencyboard.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByCreatedBy(User user);
}
