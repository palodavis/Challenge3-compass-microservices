package com.palodavis.ms_ticket_manager.service;

import com.palodavis.ms_ticket_manager.client.EventClient;
import com.palodavis.ms_ticket_manager.dto.EventDTO;
import com.palodavis.ms_ticket_manager.entity.Ticket;
import com.palodavis.ms_ticket_manager.mapper.EventMapper;
import com.palodavis.ms_ticket_manager.repository.TicketRepository;
import com.palodavis.ms_ticket_manager.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    @Autowired
    public TicketService(TicketRepository ticketRepository, EventClient eventClient) {
        this.ticketRepository = ticketRepository;
        this.eventClient = eventClient;
    }

    public Ticket createTicket(Ticket ticket) {
        if (ticket.getEvent() == null || ticket.getEvent().getEventId() == null) {
            throw new IllegalArgumentException("O campo 'event' com 'eventId' é obrigatório.");
        }
        Event event = eventClient.getEventById(ticket.getEvent().getEventId());
        if (event == null) {
            throw new IllegalArgumentException("Evento não encontrado para o ID: " + ticket.getEvent().getEventId());
        }
        EventDTO eventDTO = EventMapper.toDTO(event);
        ticket.setEvent(eventDTO);
        ticket.setTicketId(UUID.randomUUID().toString());
        ticket.setStatus("concluído");
        return ticketRepository.save(ticket);
    }

    public List<Ticket> listAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findTicketById(String id) {
        return ticketRepository.findById(id);
    }
}