package com.palodavis.ms_ticket_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String id;
    private String eventName;
    private String dateTime;

    private String street;
    private String neighborhood;
    private String city;
    private String state;
}