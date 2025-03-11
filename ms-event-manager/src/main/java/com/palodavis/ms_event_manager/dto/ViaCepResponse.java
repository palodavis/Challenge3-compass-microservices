package com.palodavis.ms_event_manager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViaCepResponse {
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private boolean erro;
}