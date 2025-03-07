package com.palodavis.ms_ticket_manager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    private String ticketId;
    private String customerName;
    private String cpf;
    private String customerMail;
    private String eventId;
    private String eventName;

    private String BRLamount;
    private String USDamount;

    public Ticket(String customerName, String cpf, String customerMail, String eventId, String eventName, String BRLamount, String USDamount) {
        this.ticketId = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.cpf = cpf;
        this.customerMail = customerMail;
        this.eventId = eventId;
        this.eventName = eventName;
        this.BRLamount = BRLamount;
        this.USDamount = USDamount;
    }
}