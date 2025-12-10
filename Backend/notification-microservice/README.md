# Notification Microservice - Spring Boot

The notification microservice is part of the **Energy Management application**.  

## Features

- Spring Security-based role identification
- Role-based access control (RBAC)
- Springdoc / OpenAPI documentation: `http://localhost/notification/public/swagger-ui/index.html`
- Docker container support
- JWT token is validated by the Traefik API-gateway
- RabbitMQ-based communication to ensure any replica of the `monitoring-microservice` can send notifications to this microservice

## Tech Stack

| Component      | Technology                   |
|----------------|------------------------------|
| Language       | Java 21                      |
| Framework      | Spring Boot                  |
| Build Tool     | Maven                        |
| Security       | Spring Security              |
| Documentation  | Springdoc OpenAPI            |
| Container      | Docker                       |
| Deployment     | Docker Compose               |
| API-gateway    | Traefik + JWT & CORS plugins |
| Message broker | RabbitMQ                     |

## Security Notes

- JWT-based authentication
- No session state (stateless microservice)
- Gateway provides request authentication boundary
- Springdoc endpoints are prefixed with: `/notification/public`
- Endpoints are prefixed with: `/notification/secured`
- The web socket connection is not secured

## Architecture

This service interacts with:

- **API Gateway (Traefik)** — all requests routed & authenticated externally: `traefik-network`; currently, no endpoints are available.
- The **web socket** connection bypasses Traefik and can be accessed at: `ws://localhost/notification/public/ws?token=` (+ JWT token - required to differentiate between notifications that target you and notifications that do not).
- **RabbitMQ `energy-management-exchange` topic** - ensures any number of replicas of the `monitoring-microservice` have a common communication media with this microservice. 
No direct public communication is allowed — internal network only.

The format of a message depends on the message type, the format of a message is shared between microservices (low coupling). All messages have the structure below:

```json
{v
    "type" : <message-type>
    // <type-dependent-field> : <value>
    // ...
    // <type-dependent-field> : <value>
}
```

Particularly, the `notification-microservice` handles the following types of messages: `OVERCONSUMPTION`.

```json
{
    "type" : "OVERCONSUMPTION",
    "username" : <string>,
    "message" : <string>
}
```

## application.properties requirements

    spring.application.name=notification-microservice
    server.port=7777
    
    springdoc.api-docs.enabled=true
    springdoc.api-docs.path=/v3/api-docs
    
    springdoc.swagger-ui.enabled=true
    springdoc.swagger-ui.config-url=/notification/public/v3/api-docs/swagger-config
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.swagger-ui.url=/notification/public/v3/api-docs
    
    springdoc.swagger-ui.disable-swagger-default-url=true
    
    spring.rabbitmq.host=synchronization-topic
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest
    
    topic.exchange.name=energy-management-exchange
    
    notification.queue.name=notification-microservice-queue
    notification.routing.key=notification-microservice.*

## How to Use

Follow these steps to run the Energy Management locally or with Docker.

### Development

1. Clone the repository
2. Build the microservice
    ```bash
    mvn clean package
    ```
3. Run locally
    ```bash
    java -jar target/<microservice-name>.jar
    ```

### Run with docker-compose

The `docker-compose.yml` file is provided in the repository.

    ```bash
    docker-compose up --build
    ```