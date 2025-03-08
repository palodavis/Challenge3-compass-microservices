package com.palodavis.ms_ticket_manager.client;

import com.palodavis.ms_ticket_manager.entity.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "http://localhost:8080")
public interface EventClient {

    @GetMapping("/events/{id}")
    Event getEventById(@PathVariable String id);
}