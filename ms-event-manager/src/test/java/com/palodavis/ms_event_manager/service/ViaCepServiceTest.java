package com.palodavis.ms_event_manager.service;

import com.palodavis.ms_event_manager.dto.ViaCepResponse;
import com.palodavis.ms_event_manager.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViaCepServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ViaCepService viaCepService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void consultCep_ShouldReturnViaCepResponse() {
        String cep = "75075-165";
        ViaCepResponse viaCepResponse = new ViaCepResponse();
        viaCepResponse.setCep(cep);

        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class))).thenReturn(viaCepResponse);

        ViaCepResponse response = viaCepService.consultCep(cep);

        assertNotNull(response);
        assertEquals(cep, response.getCep());
    }

    @Test
    void consultCep_ShouldThrowNotFoundException_WhenCepIsInvalid() {
        String cep = "00000000";
        ViaCepResponse viaCepResponse = new ViaCepResponse();
        viaCepResponse.setErro(true);

        when(restTemplate.getForObject(anyString(), eq(ViaCepResponse.class))).thenReturn(viaCepResponse);

        assertThrows(NotFoundException.class, () -> viaCepService.consultCep(cep));
    }
}