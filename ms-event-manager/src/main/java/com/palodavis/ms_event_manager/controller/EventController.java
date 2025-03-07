package com.palodavis.ms_event_manager.controller;

import com.palodavis.ms_event_manager.entity.Event;
import com.palodavis.ms_event_manager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventService.saveEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @GetMapping
    public ResponseEntity<List<Event>> listEvents() {
        return ResponseEntity.ok(eventService.listAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> findEventById(@PathVariable String id) {
        return eventService.findEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Event>> listEventsSortedByEventNameAsc() {
        return ResponseEntity.ok(eventService.listEventsSortedByName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable String id, @RequestBody Event updatedEvent) {
        return eventService.updateEvent(id, updatedEvent)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        if (eventService.deleteEvent(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}