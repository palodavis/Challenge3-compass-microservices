package com.palodavis.ms_ticket_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String eventId;
    private String eventName;
    private String eventDateTime;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
}