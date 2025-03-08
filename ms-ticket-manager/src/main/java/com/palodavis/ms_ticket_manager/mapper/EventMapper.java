package com.palodavis.ms_ticket_manager.mapper;

import com.palodavis.ms_ticket_manager.dto.EventDTO;
import com.palodavis.ms_ticket_manager.entity.Event;

public class EventMapper {

    public static EventDTO toDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventId(event.getId());
        eventDTO.setEventName(event.getEventName());
        eventDTO.setEventDateTime(event.getDateTime());
        eventDTO.setStreet(event.getStreet());
        eventDTO.setNeighborhood(event.getNeighborhood());
        eventDTO.setCity(event.getCity());
        eventDTO.setState(event.getState());
        return eventDTO;
    }
}