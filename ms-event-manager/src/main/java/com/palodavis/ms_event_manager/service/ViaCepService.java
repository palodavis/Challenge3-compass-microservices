package com.palodavis.ms_event_manager.service;

import com.palodavis.ms_event_manager.dto.ViaCepResponse;
import com.palodavis.ms_event_manager.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    private static final String VIACEP_URL = "https://viacep.com.br/ws/%s/json/";

    public ViaCepResponse consultCep(String cep) {
        String url = String.format(VIACEP_URL, cep);
        RestTemplate restTemplate = new RestTemplate();
        ViaCepResponse response = restTemplate.getForObject(url, ViaCepResponse.class);

        if (response != null && response.isErro()) {
            throw new NotFoundException("CEP não encontrado: " + cep);
        }

        return response;
    }
}