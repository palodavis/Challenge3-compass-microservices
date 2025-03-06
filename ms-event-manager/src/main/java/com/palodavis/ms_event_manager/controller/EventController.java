package com.palodavis.ms_event_manager.controller;

import com.palodavis.ms_event_manager.entity.Event;
import com.palodavis.ms_event_manager.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @GetMapping
    public List<Event> listEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public Event findEventById(@PathVariable String id) {
        return eventRepository.findById(id).orElse(null);
    }
}
