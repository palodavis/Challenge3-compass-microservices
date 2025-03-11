package com.palodavis.ms_event_manager.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    private String id;

    @NotBlank(message = "O nome do evento é obrigatório.")
    @Size(max = 100, message = "O nome do evento não pode ter mais de 100 caracteres.")
    private String eventName;

    @NotBlank(message = "A data e hora do evento são obrigatórias.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "A data e hora devem estar no formato 'yyyy-MM-ddTHH:mm:ss'.")
    private String dateTime;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "O CEP deve estar no formato '00000-000'.")
    private String cep;

    private String street;
    private String neighborhood;
    private String city;
    private String state;
}