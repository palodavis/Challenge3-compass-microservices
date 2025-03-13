package com.palodavis.ms_ticket_manager.controller;

import com.palodavis.ms_ticket_manager.dto.EventDTO;
import com.palodavis.ms_ticket_manager.entity.Ticket;
import com.palodavis.ms_ticket_manager.exceptions.InvalidDataException;
import com.palodavis.ms_ticket_manager.exceptions.NotFoundException;
import com.palodavis.ms_ticket_manager.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket_ValidTicket_ReturnsCreated() {
        Ticket ticket = new Ticket();
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(ticket);

        ResponseEntity<Ticket> response = ticketController.createTicket(ticket);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void listTickets_ReturnsListOfTickets() {
        when(ticketService.listAllTickets()).thenReturn(Collections.singletonList(new Ticket()));

        ResponseEntity<List<Ticket>> response = ticketController.listTickets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getTicketById_ValidId_ReturnsTicket() {
        when(ticketService.findIdTicket("ticketId")).thenReturn(Collections.singletonList(new Ticket()));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketById("ticketId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void cancelTicketById_ValidId_ReturnsNoContent() {
        doNothing().when(ticketService).cancelTicketById("ticketId");

        ResponseEntity<Void> response = ticketController.cancelTicketById("ticketId");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getTicketsByCpf_ValidCpf_ReturnsTickets() {
        when(ticketService.findTicketsByCpf("12345678909")).thenReturn(Collections.singletonList(new Ticket()));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByCpf("12345678909");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void updateTicket_ValidTicket_ReturnsUpdatedTicket() {
        String ticketId = "ticketId";
        Ticket updatedTicket = new Ticket();
        updatedTicket.setCustomerName("Jane Doe");
        updatedTicket.setEvent(new EventDTO());
        updatedTicket.getEvent().setEventId("eventId");

        when(ticketService.updateTicket(ticketId, updatedTicket)).thenReturn(updatedTicket);

        ResponseEntity<Ticket> response = ticketController.updateTicket(ticketId, updatedTicket);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane Doe", response.getBody().getCustomerName());
        verify(ticketService, times(1)).updateTicket(ticketId, updatedTicket);
    }

    @Test
    void updateTicket_TicketNotFound_ThrowsNotFoundException() {
        String ticketId = "ticketId";
        Ticket updatedTicket = new Ticket();
        updatedTicket.setEvent(new EventDTO());
        updatedTicket.getEvent().setEventId("eventId");

        when(ticketService.updateTicket(ticketId, updatedTicket))
                .thenThrow(new NotFoundException("Ticket não encontrado para o ID: " + ticketId));

        assertThrows(NotFoundException.class, () -> ticketController.updateTicket(ticketId, updatedTicket));
        verify(ticketService, times(1)).updateTicket(ticketId, updatedTicket);
    }

    @Test
    void updateTicket_InvalidData_ThrowsInvalidDataException() {
        String ticketId = "ticketId";
        Ticket updatedTicket = new Ticket();
        updatedTicket.setEvent(new EventDTO());

        when(ticketService.updateTicket(ticketId, updatedTicket))
                .thenThrow(new InvalidDataException("O campo 'event' com 'eventId' é obrigatório."));

        assertThrows(InvalidDataException.class, () -> ticketController.updateTicket(ticketId, updatedTicket));
        verify(ticketService, times(1)).updateTicket(ticketId, updatedTicket);
    }
    @Test
    void getTicketsByCpf_NoTicketsFound_ReturnsNotFound() {
        when(ticketService.findTicketsByCpf("12345678909")).thenThrow(new NotFoundException("Nenhum ticket encontrado para o CPF: 12345678909"));

        assertThrows(NotFoundException.class, () -> ticketController.getTicketsByCpf("12345678909"));
    }

    @Test
    void cancelTicketsByCpf_ValidCpf_ReturnsNoContent() {
        doNothing().when(ticketService).cancelTicketsByCpf("12345678909");

        ResponseEntity<Void> response = ticketController.cancelTicketsByCpf("12345678909");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void cancelTicketsByCpf_NoTicketsFound_ReturnsNotFound() {
        doThrow(new NotFoundException("Nenhum ticket encontrado para o CPF: 12345678909")).when(ticketService).cancelTicketsByCpf("12345678909");

        assertThrows(NotFoundException.class, () -> ticketController.cancelTicketsByCpf("12345678909"));
    }

    @Test
    void getTicketsByEventId_ValidEventId_ReturnsTickets() {
        when(ticketService.findTicketById("eventId")).thenReturn(Collections.singletonList(new Ticket()));

        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByEventId("eventId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getTicketsByEventId_NoTicketsFound_ReturnsNotFound() {
        when(ticketService.findTicketById("eventId")).thenThrow(new NotFoundException("Nenhum ticket encontrado para o ID do evento: eventId"));

        assertThrows(NotFoundException.class, () -> ticketController.getTicketsByEventId("eventId"));
    }


}