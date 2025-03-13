# Event and Ticket Management System

Este projeto é um sistema de gerenciamento de eventos e ingressos, desenvolvido em **Java 17**, com deploy na **AWS** e utilizando o **MongoDB Atlas** como banco de dados. O sistema é composto por dois microsserviços principais: `ms-event-manager` (gerenciamento de eventos) e `ms-ticket-manager` (gerenciamento de ingressos).

---

## Tecnologias Utilizadas

- **Linguagem**: Java 17
- **Banco de Dados**: MongoDB Atlas
- **Cloud**: AWS (Amazon Web Services)
- **Frameworks**: Spring Boot, Spring Web, Spring Data MongoDB
- **Ferramentas**: Maven, Docker, Swagger.

---

## Microsserviços


#### **Microsserviço: `ms-event-manager`**

Responsável pelo gerenciamento de eventos (CRUD), permitindo a criação, consulta, atualização e cancelamento de eventos.

| Método | Endpoint                                | Descrição                                                                 |
|--------|-----------------------------------------|---------------------------------------------------------------------------|
| POST   | `<ip>:8080/events`                      | Cria um novo evento.                                                     |
| GET    | `<ip>:8080/events`                      | Lista todos os eventos cadastrados.                                      |
| GET    | `<ip>:8080/events/{id}`                 | Busca um evento pelo ID.                                                 |
| GET    | `<ip>:8080/events/sorted`               | Lista eventos ordenados pelo nome.                                       |
| PUT    | `<ip>:8080/events/{id}`                 | Atualiza os dados de um evento existente.                                |
| DELETE | `<ip>:8080/events/delete-event/{id}`    | Exclui um evento pelo ID (se não houver ingressos vendidos).             |

### **Microsserviço: `ms-ticket-manager`**

Responsável pelo gerenciamento de ingressos (CRUD), permitindo a criação, consulta, atualização e cancelamento de ingressos.

#### Endpoints para testes locais 

| Método | Endpoint                                | Descrição                                                                 |
|--------|-----------------------------------------|---------------------------------------------------------------------------|
| POST   | `<ip>:8081/ticket`                      | Cria um novo ingresso.                                                   |
|        |                                         | **Validação**: Verifica a existência do evento no microsserviço `ms-event-manager` antes de criar o ingresso. |
| GET    | `<ip>:8081/ticket/ticket-id{id}`        | Consulta um ingresso pelo ID.                                            |
| GET    | `<ip>:8081/ticket-cpf/{cpf}`            | Consulta ingressos associados a um CPF.                                  |
| PUT    | `<ip>:8081/{id}`          | Atualiza um ingresso pelo ID.                                            |
| DELETE | `<ip>:8081/cancel-ticket/{id}`          | Cancela um ingresso pelo ID (soft-delete).                               |
| DELETE | `<ip>:8081/cancel-ticket/{cpf}`         | Cancela ingressos associados a um CPF (soft-delete).                     |
| GET    | `<ip>:8081/check-tickets-by-event/{eventId}`| Verifica se existem ingressos vinculados a um evento específico.       |
