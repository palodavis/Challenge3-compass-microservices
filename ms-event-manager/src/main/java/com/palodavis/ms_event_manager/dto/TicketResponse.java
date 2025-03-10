package com.palodavis.ms_event_manager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponse {
    private String ticketId;
    private String customerName;
    private String cpf;
    private String customerMail;
    private String eventId;
    private String brltotalAmount;
    private String usdtotalAmount;
    private String status;
    private LocalDateTime deletedAt;
}