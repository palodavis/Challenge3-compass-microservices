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

# Execução do Projeto

## Pré-requisitos

Certifique-se de ter os seguintes requisitos antes de executar o projeto:

- **Git** instalado para clonar o repositório.
- **IDE** (preferencialmente **IntelliJ IDEA**).
- **Maven** configurado para gerenciar as dependências.
- **Docker** e **Docker Compose** instalados na máquina.
- MongoDB atlas configurado em nuvem.
- Permissões adequadas para rodar os comandos na pasta raiz do projeto.

## Como executar localmente

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/palodavis/Challenge3-compass-microservices.git
   ```
2. **Abrir o projeto na IDE** (IntelliJ IDEA recomendado).
3. **Rodar as dependências do Maven**.
4. **Executar o Docker Compose:**
   ```bash
   docker-compose up --build
   ```
   
---

## Endpoints disponíveis

- Os endpoints podem ser acessados localmente apenas pelas portas **8080** e **8081**.
- No deploy na AWS, a URL será diferente e deve ser configurada com o **IP público** da instância EC2.

---

# Documentação da API - Swagger
Utilizado a documentação da API utilizando **Swagger**, proporcionando uma interface interativa para explorar os endpoints disponíveis. 
O Swagger facilita a exploração e teste da API diretamente pelo navegador.

### Acessando a Documentação de ambos microsserviços:
- Microserviço de Events pode ser acessado pelo link:
- [http://localhost:8080/docs-events.html](http://localhost:8080/docs-events.html)

![Descrição da Imagem](ms-event-manager/src/main/java/com/palodavis/ms_event_manager/utils/swagger-events.png)
- 
- Microserviço de Tickets pode ser acessado pelo link:
- [http://localhost:8080/docs-tickets.html](http://localhost:8080/docs-tickets.html)

![Descrição da Imagem](ms-event-manager/src/main/java/com/palodavis/ms_event_manager/utils/swagger-events.png)

### Endpoints Documentados:
#### Events:
- **POST /events** - Criar um novo evento.
- **GET /events** - Listar todos os eventos.
- **GET /events/{id}** - Buscar evento por ID.
- **GET /events/sorted** - Listar eventos ordenados por nome.
- **PUT /events/{id}** - Atualizar um evento.
- **DELETE /events/delete-event/{id}** - Excluir um evento.

#### Tickets:
- **POST /tickets** - Criar um novo ingresso.
- **GET /tickets** - Listar todos os ingressos.
- **GET /tickets/ticket-id/{id}** - Buscar ingresso por ID.
- **GET /tickets/ticket-cpf/{cpf}** - Buscar ingressos por CPF.
- **PUT /tickets/{id}** - Atualizar um ingresso.
- **DELETE /tickets/ticket-id/{id}** - Cancelar ingresso por ID.
- **DELETE /tickets/ticket-cpf/{cpf}** - Cancelar ingressos por CPF.
- **GET /tickets/check-tickets-by-event/{eventId}** - Buscar ingressos por ID do evento.

---

## Deploy na AWS com EC2

### Requisitos

- Conta na AWS
- Criar uma **VPC**
- Criar um **Security Group**
- Criar um **Key Pair**
- Criar uma **instância EC2** associando com a VPC, Security Group e Key Pair criados.

### Conectando-se à EC2

1. Conectar-se à instância via SSH:
   ```bash
   ssh ec2-user@<IP_PUBLICO>
   ```

2. **Instalar o Docker na EC2:**
   ```bash
   sudo yum update -y
   sudo yum install docker -y
   sudo service docker start
   sudo usermod -a -G docker ec2-user
   ```

3. **Instalar o Docker Compose na EC2:**
   ```bash
   sudo curl -L "https://github.com/docker/compose/releases/download/v2.14.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
   sudo chmod +x /usr/local/bin/docker-compose
   ```

4. **Enviar a pasta do projeto para a EC2:**
   ```bash
   scp -r ~/Challenge3-compass-microservices ec2-user@<IP_PUBLICO>:/home/ec2-user
   ```

5. **Acessar a pasta do projeto na EC2:**
   ```bash
   cd /home/ec2-user/Challenge3-compass-microservices
   ```

6. **Executar o Docker Compose na EC2:**
   ```bash
   sudo docker-compose up --build
   ```

---

