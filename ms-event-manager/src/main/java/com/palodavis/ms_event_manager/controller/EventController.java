package com.palodavis.ms_event_manager.controller;

import com.palodavis.ms_event_manager.dto.ViaCepResponse;
import com.palodavis.ms_event_manager.entity.Event;
import com.palodavis.ms_event_manager.repository.EventRepository;
import com.palodavis.ms_event_manager.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ViaCepService viaCepService;

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        ViaCepResponse endereco = viaCepService.consultarCep(event.getCep());

        if (endereco != null) {
            event.setLogradouro(endereco.getLogradouro());
            event.setBairro(endereco.getBairro());
            event.setCidade(endereco.getLocalidade());
            event.setUf(endereco.getUf());
        }
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