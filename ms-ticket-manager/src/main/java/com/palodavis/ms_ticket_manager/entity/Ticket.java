package com.palodavis.ms_ticket_manager.entity;

import com.palodavis.ms_ticket_manager.dto.EventDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    private String ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private EventDTO event;
    private String BRLtotalAmount;
    private String USDtotalAmount;
    private String status;
}