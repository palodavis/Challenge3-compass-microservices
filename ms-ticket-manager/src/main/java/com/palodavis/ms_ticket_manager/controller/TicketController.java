package com.palodavis.ms_ticket_manager.controller;

import com.palodavis.ms_ticket_manager.entity.Ticket;
import com.palodavis.ms_ticket_manager.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Ticket Controller", description = "API para gerenciamento de ingressos")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @Operation(summary = "Criar um ingresso", description = "Cria um novo ingresso com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingresso criado com sucesso",
                    content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        Ticket savedTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
    }

    @GetMapping
    @Operation(summary = "Listar todos os ingressos", description = "Retorna uma lista de todos os ingressos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de ingressos retornada com sucesso",
            content = @Content(schema = @Schema(implementation = Ticket.class)))
    public ResponseEntity<List<Ticket>> listTickets() {
        List<Ticket> tickets = ticketService.listAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ticket-id/{id}")
    @Operation(summary = "Buscar ingresso por ID", description = "Retorna um ingresso com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingresso encontrado",
                    content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    public ResponseEntity<List<Ticket>> getTicketById(
            @Parameter(description = "ID do ingresso a ser buscado", required = true)
            @PathVariable String id) {
        List<Ticket> tickets = ticketService.findIdTicket(id);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ticket-cpf/{cpf}")
    @Operation(summary = "Buscar ingressos por CPF", description = "Retorna uma lista de ingressos com base no CPF fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingressos encontrados",
                    content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum ingresso encontrado para o CPF fornecido")
    })
    public ResponseEntity<List<Ticket>> getTicketsByCpf(
            @Parameter(description = "CPF do titular dos ingressos", required = true)
            @PathVariable String cpf) {
        List<Ticket> tickets = ticketService.findTicketsByCpf(cpf);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um ingresso", description = "Atualiza os dados de um ingresso existente com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingresso atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    public ResponseEntity<Ticket> updateTicket(
            @Parameter(description = "ID do ingresso a ser atualizado", required = true)
            @PathVariable String id,
            @Valid @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketService.updateTicket(id, ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/ticket-id/{id}")
    @Operation(summary = "Cancelar ingresso por ID", description = "Cancela um ingresso com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ingresso cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    public ResponseEntity<Void> cancelTicketById(
            @Parameter(description = "ID do ingresso a ser cancelado", required = true)
            @PathVariable String id) {
        ticketService.cancelTicketById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ticket-cpf/{cpf}")
    @Operation(summary = "Cancelar ingressos por CPF", description = "Cancela todos os ingressos associados ao CPF fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ingressos cancelados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum ingresso encontrado para o CPF fornecido")
    })
    public ResponseEntity<Void> cancelTicketsByCpf(
            @Parameter(description = "CPF do titular dos ingressos", required = true)
            @PathVariable String cpf) {
        ticketService.cancelTicketsByCpf(cpf);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-tickets-by-event/{eventId}")
    @Operation(summary = "Buscar ingressos por ID do evento", description = "Retorna uma lista de ingressos associados ao ID do evento fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingressos encontrados",
                    content = @Content(schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum ingresso encontrado para o evento fornecido")
    })
    public ResponseEntity<List<Ticket>> getTicketsByEventId(
            @Parameter(description = "ID do evento", required = true)
            @PathVariable String eventId) {
        List<Ticket> tickets = ticketService.findTicketById(eventId);
        return ResponseEntity.ok(tickets);
    }
}