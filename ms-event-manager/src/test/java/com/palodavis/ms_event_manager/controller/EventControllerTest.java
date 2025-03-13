package com.palodavis.ms_event_manager.controller;

import com.palodavis.ms_event_manager.entity.Event;
import com.palodavis.ms_event_manager.exception.NotFoundException;
import com.palodavis.ms_event_manager.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEvent_ShouldReturnCreatedEvent() {
        Event event = new Event();
        event.setEventName("Test Event");

        when(eventService.saveEvent(event)).thenReturn(event);

        ResponseEntity<Event> response = eventController.createEvent(event);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    void listEvents_ShouldReturnAllEvents() {
        Event event = new Event();
        when(eventService.listAllEvents()).thenReturn(Collections.singletonList(event));

        ResponseEntity<List<Event>> response = eventController.listEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void findEventById_ShouldReturnEvent() {
        Event event = new Event();
        event.setId("1");

        when(eventService.findEventById("1")).thenReturn(Optional.of(event));

        ResponseEntity<Event> response = eventController.findEventById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    void findEventById_ShouldThrowNotFoundException() {
        when(eventService.findEventById("1")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventController.findEventById("1"));
    }

    @Test
    void listEventsSortedByEventNameAsc_ShouldReturnSortedEvents() {
        Event event = new Event();
        when(eventService.listEventsSortedByName()).thenReturn(Collections.singletonList(event));

        ResponseEntity<List<Event>> response = eventController.listEventsSortedByEventNameAsc();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void updateEvent_ShouldReturnUpdatedEvent() {
        Event existingEvent = new Event();
        existingEvent.setId("1");

        when(eventService.updateEvent("1", existingEvent)).thenReturn(Optional.of(existingEvent));

        ResponseEntity<Event> response = eventController.updateEvent("1", existingEvent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingEvent, response.getBody());
    }

    @Test
    void updateEvent_ShouldThrowNotFoundException() {
        Event event = new Event();
        when(eventService.updateEvent("1", event)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventController.updateEvent("1", event));
    }

    @Test
    void deleteEvent_ShouldReturnNoContent() {
        doNothing().when(eventService).deleteEvent("1");

        ResponseEntity<Void> response = eventController.deleteEvent("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteEvent_ShouldThrowNotFoundException() {
        doThrow(NotFoundException.class).when(eventService).deleteEvent("1");

        assertThrows(NotFoundException.class, () -> eventController.deleteEvent("1"));
    }
}