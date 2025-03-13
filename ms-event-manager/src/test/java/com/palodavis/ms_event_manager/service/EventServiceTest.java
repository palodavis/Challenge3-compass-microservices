package com.palodavis.ms_event_manager.service;

import com.palodavis.ms_event_manager.client.TicketClient;
import com.palodavis.ms_event_manager.dto.TicketResponse;
import com.palodavis.ms_event_manager.dto.ViaCepResponse;
import com.palodavis.ms_event_manager.entity.Event;
import com.palodavis.ms_event_manager.exception.ConflictException;
import com.palodavis.ms_event_manager.exception.NotFoundException;
import com.palodavis.ms_event_manager.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ViaCepService viaCepService;

    @Mock
    private TicketClient ticketClient;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveEvent_ShouldReturnSavedEvent() {
        Event event = new Event();
        event.setCep("12345678");
        ViaCepResponse viaCepResponse = new ViaCepResponse();
        viaCepResponse.setLogradouro("Rua Teste");
        viaCepResponse.setBairro("Bairro Teste");
        viaCepResponse.setLocalidade("Cidade Teste");
        viaCepResponse.setUf("TS");

        when(viaCepService.consultCep(event.getCep())).thenReturn(viaCepResponse);
        when(eventRepository.save(event)).thenReturn(event);

        Event savedEvent = eventService.saveEvent(event);

        assertNotNull(savedEvent);
        assertEquals("Rua Teste", savedEvent.getStreet());
        assertEquals("Bairro Teste", savedEvent.getNeighborhood());
        assertEquals("Cidade Teste", savedEvent.getCity());
        assertEquals("TS", savedEvent.getState());
    }

    @Test
    void saveEvent_ShouldNotFillAddress_WhenCepIsNullOrEmpty() {
        Event event = new Event();
        event.setCep(null);

        when(eventRepository.save(event)).thenReturn(event);

        Event savedEvent = eventService.saveEvent(event);

        assertNotNull(savedEvent);
        assertNull(savedEvent.getStreet());
        assertNull(savedEvent.getNeighborhood());
        assertNull(savedEvent.getCity());
        assertNull(savedEvent.getState());
    }

    @Test
    void findEventById_ShouldReturnEvent() {
        Event event = new Event();
        event.setId("1");

        when(eventRepository.findById("1")).thenReturn(Optional.of(event));

        Optional<Event> foundEvent = eventService.findEventById("1");

        assertTrue(foundEvent.isPresent());
        assertEquals("1", foundEvent.get().getId());
    }

    @Test
    void findEventById_ShouldReturnEmpty_WhenEventNotFound() {
        when(eventRepository.findById("1")).thenReturn(Optional.empty());

        Optional<Event> foundEvent = eventService.findEventById("1");

        assertTrue(foundEvent.isEmpty());
    }

    @Test
    void listAllEvents_ShouldReturnAllEvents() {
        Event event = new Event();
        when(eventRepository.findAll()).thenReturn(Collections.singletonList(event));

        List<Event> events = eventService.listAllEvents();

        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
    }

    @Test
    void listEventsSortedByName_ShouldReturnSortedEvents() {
        Event event = new Event();
        when(eventRepository.findAllByOrderByEventNameAsc()).thenReturn(Collections.singletonList(event));

        List<Event> events = eventService.listEventsSortedByName();

        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
    }

    @Test
    void updateEvent_ShouldReturnUpdatedEvent() {
        Event existingEvent = new Event();
        existingEvent.setId("1");
        existingEvent.setEventName("Old Event");

        Event updatedEvent = new Event();
        updatedEvent.setEventName("New Event");

        when(eventRepository.findById("1")).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(existingEvent);

        Optional<Event> result = eventService.updateEvent("1", updatedEvent);

        assertTrue(result.isPresent());
        assertEquals("New Event", result.get().getEventName());
    }

    @Test
    void updateEvent_ShouldReturnEmpty_WhenEventNotFound() {
        Event updatedEvent = new Event();
        updatedEvent.setEventName("New Event");

        when(eventRepository.findById("1")).thenReturn(Optional.empty());

        Optional<Event> result = eventService.updateEvent("1", updatedEvent);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteEvent_ShouldThrowConflictException_WhenTicketsExist() {
        Event event = new Event();
        event.setId("1");

        when(eventRepository.findById("1")).thenReturn(Optional.of(event));
        when(ticketClient.getTicketsByEventId("1")).thenReturn(Collections.singletonList(new TicketResponse()));

        assertThrows(ConflictException.class, () -> eventService.deleteEvent("1"));
    }

    @Test
    void deleteEvent_ShouldDeleteEvent_WhenNoTicketsExist() {
        Event event = new Event();
        event.setId("1");

        when(eventRepository.findById("1")).thenReturn(Optional.of(event));
        when(ticketClient.getTicketsByEventId("1")).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> eventService.deleteEvent("1"));
        verify(eventRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteEvent_ShouldThrowNotFoundException_WhenEventNotFound() {
        when(eventRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.deleteEvent("1"));
    }

    @Test
    void fillAddress_ShouldNotFillAddress_WhenCepIsInvalid() {
        Event event = new Event();
        event.setCep("invalid-cep");

        when(viaCepService.consultCep(event.getCep())).thenReturn(null);

        eventService.saveEvent(event);

        assertNull(event.getStreet());
        assertNull(event.getNeighborhood());
        assertNull(event.getCity());
        assertNull(event.getState());
    }

    @Test
    void fillAddress_ShouldNotFillAddress_WhenViaCepReturnsNull() {
        Event event = new Event();
        event.setCep("12345678");

        when(viaCepService.consultCep(event.getCep())).thenReturn(null);

        eventService.saveEvent(event);

        assertNull(event.getStreet());
        assertNull(event.getNeighborhood());
        assertNull(event.getCity());
        assertNull(event.getState());
    }
}