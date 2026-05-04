package com.agencyboard.agencyboard.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientRequest {

    @NotBlank
    private String name;

    private String email;
    private String phone;
    private String company;
    private String industry;
    private String notes;
}