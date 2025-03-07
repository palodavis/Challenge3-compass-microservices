package com.palodavis.ms_event_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events-manager")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    private String id;
    private String eventName;
    private String dateTime;
    private String cep;

    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
}