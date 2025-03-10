package com.palodavis.ms_event_manager.service;

import com.palodavis.ms_event_manager.client.TicketClient;
import com.palodavis.ms_event_manager.dto.TicketResponse;
import com.palodavis.ms_event_manager.dto.ViaCepResponse;
import com.palodavis.ms_event_manager.entity.Event;
import com.palodavis.ms_event_manager.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepService viaCepService;
    private final TicketClient ticketClient;

    @Autowired
    public EventService(EventRepository eventRepository, ViaCepService viaCepService, TicketClient ticketClient) {
        this.eventRepository = eventRepository;
        this.viaCepService = viaCepService;
        this.ticketClient = ticketClient;

    }

    public Event saveEvent(Event event) {
        fillAddress(event);
        return eventRepository.save(event);
    }

    public Optional<Event> findEventById(String id) {
        return eventRepository.findById(id);
    }

    public List<Event> listAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> listEventsSortedByName() {
        return eventRepository.findAllByOrderByEventNameAsc();
    }

    public Optional<Event> updateEvent(String id, Event updatedEvent) {
        return eventRepository.findById(id).map(event -> {
            event.setEventName(updatedEvent.getEventName());
            event.setDateTime(updatedEvent.getDateTime());
            event.setCep(updatedEvent.getCep());
            fillAddress(event);
            return eventRepository.save(event);
        });
    }

    public void deleteEvent(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));

        List<TicketResponse> tickets = ticketClient.getTicketsByEventId(id);
        if (!tickets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível excluir o evento: ingressos vendidos");
        }
        eventRepository.deleteById(id);
    }

//    public boolean deleteEvent(String id) {
//        if (eventRepository.existsById(id)) {
//            eventRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }

    private void fillAddress(Event event) {
        if (event.getCep() == null || event.getCep().trim().isEmpty()) {
            return;
        }
        ViaCepResponse address = viaCepService.consultCep(event.getCep());
        if (address != null) {
            event.setStreet(address.getLogradouro());
            event.setNeighborhood(address.getBairro());
            event.setCity(address.getLocalidade());
            event.setState(address.getUf());
        }
    }
}
