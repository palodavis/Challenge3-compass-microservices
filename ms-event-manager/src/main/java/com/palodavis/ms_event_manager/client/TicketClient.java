package com.palodavis.ms_event_manager.client;

import com.palodavis.ms_event_manager.dto.TicketResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-ticket-manager", url = "http://localhost:8081")
public interface TicketClient {
    @GetMapping("/tickets/check-tickets-by-event/{eventId}")
    List<TicketResponse> getTicketsByEventId(@PathVariable String eventId);
}