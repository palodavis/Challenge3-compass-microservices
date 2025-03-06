package com.palodavis.ms_event_manager.repository;

import com.palodavis.ms_event_manager.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

}
