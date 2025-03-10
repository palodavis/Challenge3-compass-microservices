package com.palodavis.ms_event_manager.controller;

import com.palodavis.ms_event_manager.entity.Event;
import com.palodavis.ms_event_manager.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/events")
@Tag(name = "Event Controller", description = "API para gerenciamento de eventos")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @Operation(summary = "Criar um evento", description = "Cria um novo evento com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento criado com sucesso",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventService.saveEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @GetMapping
    @Operation(summary = "Listar todos os eventos", description = "Retorna uma lista de todos os eventos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso",
            content = @Content(schema = @Schema(implementation = Event.class)))
    public ResponseEntity<List<Event>> listEvents() {
        return ResponseEntity.ok(eventService.listAllEvents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID", description = "Retorna um evento com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Event> findEventById(
            @Parameter(description = "ID do evento a ser buscado", required = true)
            @PathVariable String id) {
        return eventService.findEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sorted")
    @Operation(summary = "Listar eventos ordenados por nome", description = "Retorna uma lista de eventos ordenados pelo nome")
    @ApiResponse(responseCode = "200", description = "Lista de eventos ordenada retornada com sucesso",
            content = @Content(schema = @Schema(implementation = Event.class)))
    public ResponseEntity<List<Event>> listEventsSortedByEventNameAsc() {
        return ResponseEntity.ok(eventService.listEventsSortedByName());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um evento", description = "Atualiza os dados de um evento existente com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Event> updateEvent(
            @Parameter(description = "ID do evento a ser atualizado", required = true)
            @PathVariable String id,
            @RequestBody Event updatedEvent) {
        return eventService.updateEvent(id, updatedEvent)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-event/{id}")
    @Operation(summary = "Excluir um evento", description = "Exclui um evento com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado"),
            @ApiResponse(responseCode = "409", description = "Não é possível excluir o evento: ingressos vendidos")
    })
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID do evento a ser excluído", required = true)
            @PathVariable String id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
}