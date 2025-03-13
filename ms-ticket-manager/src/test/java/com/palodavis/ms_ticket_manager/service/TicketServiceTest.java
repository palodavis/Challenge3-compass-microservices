package com.palodavis.ms_ticket_manager.service;

import com.palodavis.ms_ticket_manager.client.EventClient;
import com.palodavis.ms_ticket_manager.dto.EventDTO;
import com.palodavis.ms_ticket_manager.entity.Event;
import com.palodavis.ms_ticket_manager.entity.Ticket;
import com.palodavis.ms_ticket_manager.exceptions.InvalidDataException;
import com.palodavis.ms_ticket_manager.exceptions.NotFoundException;
import com.palodavis.ms_ticket_manager.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket_ValidTicket_ReturnsSavedTicket() {
        Ticket ticket = new Ticket();
        ticket.setCpf("12345678909");
        ticket.setEvent(new EventDTO());
        ticket.getEvent().setEventId("eventId");

        Event event = new Event();
        event.setId("eventId");

        when(eventClient.getEventById("eventId")).thenReturn(event);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket savedTicket = ticketService.createTicket(ticket);

        assertNotNull(savedTicket);
        assertEquals("concluído", savedTicket.getStatus());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void createTicket_InvalidCpf_ThrowsInvalidDataException() {
        Ticket ticket = new Ticket();
        ticket.setCpf("12345678900");

        assertThrows(InvalidDataException.class, () -> ticketService.createTicket(ticket));
    }

    @Test
    void createTicket_EventNotFound_ThrowsNotFoundException() {
        Ticket ticket = new Ticket();
        ticket.setCpf("12345678909");
        ticket.setEvent(new EventDTO());
        ticket.getEvent().setEventId("eventId");

        when(eventClient.getEventById("eventId")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> ticketService.createTicket(ticket));
    }

    @Test
    void listAllTickets_ReturnsListOfTickets() {
        when(ticketRepository.findAll()).thenReturn(Collections.singletonList(new Ticket()));

        List<Ticket> tickets = ticketService.listAllTickets();

        assertFalse(tickets.isEmpty());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void findTicketById_ValidId_ReturnsTickets() {
        when(ticketRepository.findActiveTicketsByEventId("eventId")).thenReturn(Collections.singletonList(new Ticket()));

        List<Ticket> tickets = ticketService.findTicketById("eventId");

        assertFalse(tickets.isEmpty());
        verify(ticketRepository, times(1)).findActiveTicketsByEventId("eventId");
    }

    @Test
    void findTicketById_NoTicketsFound_ThrowsNotFoundException() {
        when(ticketRepository.findActiveTicketsByEventId("eventId")).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> ticketService.findTicketById("eventId"));
    }

    @Test
    void cancelTicketById_ValidId_CancelsTicket() {
        Ticket ticket = new Ticket();
        ticket.setTicketId("ticketId");

        when(ticketRepository.findById("ticketId")).thenReturn(Optional.of(ticket));

        ticketService.cancelTicketById("ticketId");

        assertEquals("cancelado", ticket.getStatus());
        assertNotNull(ticket.getDeletedAt());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void cancelTicketById_TicketNotFound_ThrowsNotFoundException() {
        when(ticketRepository.findById("ticketId")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ticketService.cancelTicketById("ticketId"));
    }

    @Test
    void findIdTicket_ValidId_ReturnsTickets() {
        when(ticketRepository.findActiveTicketsByTicketId("ticketId")).thenReturn(Collections.singletonList(new Ticket()));

        List<Ticket> tickets = ticketService.findIdTicket("ticketId");

        assertFalse(tickets.isEmpty());
        verify(ticketRepository, times(1)).findActiveTicketsByTicketId("ticketId");
    }

    @Test
    void findIdTicket_NoTicketsFound_ThrowsNotFoundException() {
        when(ticketRepository.findActiveTicketsByTicketId("ticketId")).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> ticketService.findIdTicket("ticketId"));
    }

    @Test
    void findTicketsByCpf_ValidCpf_ReturnsTickets() {
        when(ticketRepository.findActiveTicketsByCpf("12345678909")).thenReturn(Collections.singletonList(new Ticket()));

        List<Ticket> tickets = ticketService.findTicketsByCpf("12345678909");

        assertFalse(tickets.isEmpty());
        verify(ticketRepository, times(1)).findActiveTicketsByCpf("12345678909");
    }

    @Test
    void findTicketsByCpf_NoTicketsFound_ThrowsNotFoundException() {
        when(ticketRepository.findActiveTicketsByCpf("12345678909")).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> ticketService.findTicketsByCpf("12345678909"));
    }

    @Test
    void cancelTicketsByCpf_ValidCpf_CancelsTickets() {
        Ticket ticket = new Ticket();
        ticket.setCpf("12345678909");

        when(ticketRepository.findByCpf("12345678909")).thenReturn(Collections.singletonList(ticket));

        ticketService.cancelTicketsByCpf("12345678909");

        assertEquals("cancelado", ticket.getStatus());
        assertNotNull(ticket.getDeletedAt());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void cancelTicketsByCpf_NoTicketsFound_ThrowsNotFoundException() {
        when(ticketRepository.findByCpf("12345678909")).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> ticketService.cancelTicketsByCpf("12345678909"));
    }

    @Test
    void updateTicket_ValidTicket_ReturnsUpdatedTicket() {
        String ticketId = "ticketId";
        Ticket existingTicket = new Ticket();
        existingTicket.setTicketId(ticketId);
        existingTicket.setCustomerName("Paulo");
        existingTicket.setCustomerMail("paulo@email.com");

        Ticket updatedTicket = new Ticket();
        updatedTicket.setCustomerName("Paulo Silva");
        updatedTicket.setCustomerMail("paulo.silva@email.com");
        updatedTicket.setEvent(new EventDTO());
        updatedTicket.getEvent().setEventId("eventId");

        Event event = new Event();
        event.setId("eventId");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(eventClient.getEventById("eventId")).thenReturn(event);
        when(ticketRepository.save(existingTicket)).thenReturn(existingTicket);

        Ticket result = ticketService.updateTicket(ticketId, updatedTicket);

        assertNotNull(result);
        assertEquals("Paulo Silva", result.getCustomerName());
        assertEquals("paulo.silva@email.com", result.getCustomerMail());
        verify(ticketRepository, times(1)).save(existingTicket);
    }

    @Test
    void updateTicket_TicketNotFound_ThrowsNotFoundException() {
        String ticketId = "ticketId";
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ticketService.updateTicket(ticketId, new Ticket()));
    }

    @Test
    void updateTicket_EventIdNull_ThrowsInvalidDataException() {
        String ticketId = "ticketId";
        Ticket existingTicket = new Ticket();
        existingTicket.setTicketId(ticketId);

        Ticket updatedTicket = new Ticket();
        updatedTicket.setEvent(new EventDTO());

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));

        assertThrows(InvalidDataException.class, () -> ticketService.updateTicket(ticketId, updatedTicket));
    }

    @Test
    void updateTicket_EventNotFound_ThrowsNotFoundException() {
        String ticketId = "ticketId";
        Ticket existingTicket = new Ticket();
        existingTicket.setTicketId(ticketId);

        Ticket updatedTicket = new Ticket();
        updatedTicket.setEvent(new EventDTO());
        updatedTicket.getEvent().setEventId("eventId");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
        when(eventClient.getEventById("eventId")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> ticketService.updateTicket(ticketId, updatedTicket));
    }

}