package com.palodavis.ms_ticket_manager.entity;

import com.palodavis.ms_ticket_manager.dto.EventDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    private String ticketId;

    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "O CPF deve estar no formato '000.000.000-00'.")
    private String cpf;

    @NotBlank(message = "O nome do cliente é obrigatório.")
    @Size(max = 100, message = "O nome do cliente não pode ter mais de 100 caracteres.")
    private String customerName;

    @NotBlank(message = "O e-mail do cliente é obrigatório.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "O e-mail do cliente deve ser válido.")
    private String customerMail;

    @NotNull(message = "O evento é obrigatório.")
    private EventDTO event;

    @NotBlank(message = "O valor total em BRL é obrigatório.")
    private String BRLtotalAmount;

    @NotBlank(message = "O valor total em USD é obrigatório.")
    private String USDtotalAmount;

    private String status;

    @Column(nullable = true)
    private LocalDateTime deletedAt;
}