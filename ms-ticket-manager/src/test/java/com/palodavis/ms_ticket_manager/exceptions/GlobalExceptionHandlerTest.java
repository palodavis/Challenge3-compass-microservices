package com.palodavis.ms_ticket_manager.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleValidationExceptions_ReturnsBadRequest() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        FieldError fieldError = new FieldError("objectName", "fieldName", "mensagem de erro");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertNotNull(body.get("timestamp"));

        assertNotNull(body.get("errors"));
        List<?> errors = (List<?>) body.get("errors");
        assertFalse(errors.isEmpty());
        assertEquals("fieldName: mensagem de erro", errors.get(0));
    }

    @Test
    void handleNotFoundException_ReturnsNotFound() {
        NotFoundException ex = new NotFoundException("Recurso não encontrado");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/tickets/123");
        ResponseEntity<Object> response = globalExceptionHandler.handleNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), body.get("status"));
        assertEquals("Recurso não encontrado", body.get("message"));
        assertEquals("/tickets/123", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleInvalidDataException_ReturnsBadRequest() {
        InvalidDataException ex = new InvalidDataException("Dados inválidos");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/tickets");

        ResponseEntity<Object> response = globalExceptionHandler.handleInvalidDataException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("Dados inválidos", body.get("message"));
        assertEquals("/tickets", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleConflictException_ReturnsConflict() {
        ConflictException ex = new ConflictException("Conflito detectado");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/tickets");

        ResponseEntity<Object> response = globalExceptionHandler.handleConflictException(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(HttpStatus.CONFLICT.value(), body.get("status"));
        assertEquals("Conflito detectado", body.get("message"));
        assertEquals("/tickets", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleGlobalException_ReturnsInternalServerError() {
        Exception ex = new Exception("Erro inesperado");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/tickets");

        ResponseEntity<Object> response = globalExceptionHandler.handleGlobalException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.get("status"));
        assertEquals("Ocorreu um erro inesperado", body.get("message"));
        assertEquals("/tickets", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }
}