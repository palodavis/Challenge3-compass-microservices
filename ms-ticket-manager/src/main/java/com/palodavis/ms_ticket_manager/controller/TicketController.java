package com.palodavis.ms_ticket_manager.controller;

import com.palodavis.ms_ticket_manager.entity.Ticket;
import com.palodavis.ms_ticket_manager.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket savedTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> listTickets() {
        List<Ticket> tickets = ticketService.listAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ticket-id/{id}")
    public ResponseEntity<List<Ticket>> findTicketById(@PathVariable String id) {
        List<Ticket> tickets = ticketService.findTicketById(id);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/ticket-cpf/{cpf}")
    public ResponseEntity<List<Ticket>> getTicketsByCpf(@PathVariable String cpf) {
        List<Ticket> tickets = ticketService.findTicketsByCpf(cpf);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable String id, @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketService.updateTicket(id, ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/ticket-id/{id}")
    public ResponseEntity<Void> cancelTicketById(@PathVariable String id) {
        ticketService.cancelTicketById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ticket-cpf/{cpf}")
    public ResponseEntity<Void> cancelTicketsByCpf(@PathVariable String cpf) {
        ticketService.cancelTicketsByCpf(cpf);
        return ResponseEntity.noContent().build();
    }

}