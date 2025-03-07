package com.palodavis.ms_event_manager.repository;

import com.palodavis.ms_event_manager.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findAllByOrderByEventNameAsc();
}
