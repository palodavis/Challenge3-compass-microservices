package com.palodavis.ms_event_manager.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViaCepResponseTest {

    @Test
    void testGettersAndSetters() {
        ViaCepResponse response = new ViaCepResponse();

        response.setCep("75075-165");
        response.setLogradouro("Rua chile");
        response.setBairro("Boa vista");
        response.setLocalidade("Anápolis");
        response.setUf("GO");
        response.setErro(false);

        assertEquals("75075-165", response.getCep());
        assertEquals("Rua chile", response.getLogradouro());
        assertEquals("Boa vista", response.getBairro());
        assertEquals("Anápolis", response.getLocalidade());
        assertEquals("GO", response.getUf());
        assertFalse(response.isErro());
    }

    @Test
    void testErroFlag() {
        ViaCepResponse response = new ViaCepResponse();

        response.setErro(true);
        assertTrue(response.isErro(), "Erro deveria ser true");

        response.setErro(false);
        assertFalse(response.isErro(), "Erro deveria ser false");
    }
}
