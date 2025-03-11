package com.palodavis.ms_ticket_manager.repository;

import com.palodavis.ms_ticket_manager.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findByCpf(String cpf);

    @Query("{ 'cpf': ?0, 'deletedAt': null }")
    List<Ticket> findActiveTicketsByCpf(String cpf);

    @Query("{ '_id': ?0, 'deletedAt': null }")
    List<Ticket> findActiveTicketsByTicketId(String id);

    @Query("{ 'event.eventId': ?0, 'deletedAt': null }")
    List<Ticket> findActiveTicketsByEventId(String eventId);

}