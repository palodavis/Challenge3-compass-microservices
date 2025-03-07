package com.palodavis.ms_ticket_manager.repository;

import com.palodavis.ms_ticket_manager.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {
}