package com.palodavis.ms_ticket_manager.service;

import com.palodavis.ms_ticket_manager.client.EventClient;
import com.palodavis.ms_ticket_manager.dto.EventDTO;
import com.palodavis.ms_ticket_manager.entity.Event;
import com.palodavis.ms_ticket_manager.entity.Ticket;
import com.palodavis.ms_ticket_manager.exceptions.InvalidDataException;
import com.palodavis.ms_ticket_manager.exceptions.NotFoundException;
import com.palodavis.ms_ticket_manager.mapper.EventMapper;
import com.palodavis.ms_ticket_manager.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
            throw new InvalidDataException("O campo 'event' com 'eventId' é obrigatório.");
        }
        Event event = eventClient.getEventById(ticket.getEvent().getEventId());
        if (event == null) {
            throw new NotFoundException("Evento não encontrado para o ID: " + ticket.getEvent().getEventId());
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

    public List<Ticket> findTicketById(String id) {
        List<Ticket> tickets = ticketRepository.findActiveTicketsByEventId(id);
        if (tickets.isEmpty()) {
            throw new NotFoundException("Nenhum ticket encontrado para o ID do evento: " + id);
        }
        return tickets;
    }

    public List<Ticket> findIdTicket(String id) {
        List<Ticket> tickets = ticketRepository.findActiveTicketsByTicketId(id);
        if (tickets.isEmpty()) {
            throw new NotFoundException("Nenhum ticket encontrado para o ID: " + id);
        }
        return tickets;
    }

    public List<Ticket> findTicketsByCpf(String cpf) {
        List<Ticket> tickets = ticketRepository.findActiveTicketsByCpf(cpf);
        if (tickets.isEmpty()) {
            throw new NotFoundException("Nenhum ticket encontrado para o CPF: " + cpf);
        }
        return tickets;
    }

    public Ticket updateTicket(String id, Ticket ticket) {
        return ticketRepository.findById(id)
                .map(existingTicket -> {
                    if (ticket.getCustomerName() != null) {
                        existingTicket.setCustomerName(ticket.getCustomerName());
                    }
                    if (ticket.getCustomerMail() != null) {
                        existingTicket.setCustomerMail(ticket.getCustomerMail());
                    }
                    if (ticket.getEvent() != null && ticket.getEvent().getEventId() != null) {
                        Event event = eventClient.getEventById(ticket.getEvent().getEventId());
                        if (event == null) {
                            throw new NotFoundException("Evento não encontrado para o ID: " + ticket.getEvent().getEventId());
                        }
                        EventDTO eventDTO = EventMapper.toDTO(event);
                        existingTicket.setEvent(eventDTO);
                    }
                    if (ticket.getBRLtotalAmount() != null) {
                        existingTicket.setBRLtotalAmount(ticket.getBRLtotalAmount());
                    }
                    if (ticket.getUSDtotalAmount() != null) {
                        existingTicket.setUSDtotalAmount(ticket.getUSDtotalAmount());
                    }
                    return ticketRepository.save(existingTicket);
                })
                .orElseThrow(() -> new NotFoundException("Ticket não encontrado para o ID: " + id));
    }

    public void cancelTicketById(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket não encontrado para o ID: " + id));
        ticket.setStatus("cancelado");
        ticket.setDeletedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    public void cancelTicketsByCpf(String cpf) {
        List<Ticket> tickets = ticketRepository.findByCpf(cpf);
        if (tickets.isEmpty()) {
            throw new NotFoundException("Nenhum ticket encontrado para o CPF: " + cpf);
        }
        tickets.forEach(ticket -> {
            ticket.setStatus("cancelado");
            ticket.setDeletedAt(LocalDateTime.now());
            ticketRepository.save(ticket);
        });
    }
}