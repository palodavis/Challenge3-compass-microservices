package com.palodavis.ms_ticket_manager.service;

import com.palodavis.ms_ticket_manager.entity.Ticket;
import com.palodavis.ms_ticket_manager.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> listAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findTicketById(String id) {
        return ticketRepository.findById(id);
    }

}