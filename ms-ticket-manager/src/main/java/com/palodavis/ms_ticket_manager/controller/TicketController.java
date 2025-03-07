package com.palodavis.ms_ticket_manager.controller;

import com.palodavis.ms_ticket_manager.entity.Ticket;
import com.palodavis.ms_ticket_manager.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok(ticketService.listAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> findTicketById(@PathVariable String id) {
        return ticketService.findTicketById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}