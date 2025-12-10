# Device Microservice — Spring Boot

The Device microservice is part of the **Energy Management application**.  
Its responsibility is to store and manage **device entities**, and associate devices with users through **data duplication strategy** to optimize authorization and reduce cross-service calls.

This service is **not publicly exposed** and is **accessible only through the API Gateway**.  
It runs as part of the **Docker Compose microservices environment**.

## Features

- Spring Security-based role identification
- Role-based access control (RBAC)
- PostgreSQL user and device storage (data duplication)
- User-Device relationships using data duplication
- CRUD operations for device entities
- Springdoc / OpenAPI documentation: `http://localhost/device/public/swagger-ui/index.html`
- Docker container support
- JWT token is validated by traefik API-gateway
- RabbitMQ-based synchronization to ensure database consistency between this microservice, the `user-microservice`, and the `monitoring-microservice`

## Tech Stack

| Component      | Technology                   |
|----------------|------------------------------|
| Language       | Java 21                      |
| Framework      | Spring Boot                  |
| Build Tool     | Maven                        |
| Database       | PostgreSQL                   |
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
- Springdoc endpoints are prefixed with: `/device/public`
- Endpoints are prefixed with: `/device/secured`

## Architecture

This service interacts with:

- **Device Database** - separate docker network: `device-network`
- **API Gateway (Traefik)** — all requests routed & authenticated externally: `traefik-network`
- **RabbitMQ `energy-management-exchange` topic** - ensures partial consistency between this microservice, the `user-microservice`, and the `monitoring-microservice` (separate docker network: `synchronization-topic-network`)

The microservice interacts with the `user-microservice` by means of a RabbitMQ queue (subscriber): `device-microservice-queue` that is part of the `energy-management-exchange` topic. This assures partial consistency between the `device-microservice-database` and the `user-microservice-database`.

The microservice interacts with the `monitoring-microservice` by means of a RabbitMQ queue (publisher): `monitoring-microservice-queue` that is part of the `energy-management-exchange` topic. This assures partial consistency between the `device-microservice-database` and the `monitoring-microservice-database`.

Each microservice synchronizes the microservices that are directly dependent on it, thus, depending on requests the next flowchart describes the database synchronization strategy:

    authentication ----> user ----> device ----> monitoring 

The format of a message depends on the message type, the format of a message is shared between microservices (low coupling). All messages have the structure below:

```json
{
    "type" : <message-type>
    // <type-dependent-field> : <value>
    // ...
    // <type-dependent-field> : <value>
}
```

Particularly, the `device-microservice` handles the following types of messages: `USER_CREATE`, `USER_UPDATE`, `USER_DELETE`, and publishes `DEVICE_CREATE`, `DEVICE_DELETE`. `DEVICE_UPDATE` is not required as only the id of the device from de `device-microservice-database` is referenced in the `monitoring-microservice-database`. 

```json
{
    "type" : USER_CREATE,
    "username" : <string>
}
```

```json
{
    "type" : USER_UPDATE,
    "oldUsername": <string>,
    "newUsername": <string>
}
```

```json
{
    "type" : USER_DELETE,
    "ids" : [
        <id#1>,
        <id#2>,
        ...
    ]
}
```

```json
{
    "type" : DEVICE_CREATE,
    "referencedId" : <number>
}
```

```json
{
    "type" : DEVICE_DELETE,
    "ids" : [
        <id#1>,
        <id#2>,
        ...
    ]
}
```

## application.properties requirements

    spring.application.name=authentication-microservice
    server.port=your-server-port

    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.datasource.url=docker-database-endpoint
    spring.datasource.username=database-username
    spring.datasource.password=database-password

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.open-in-view=false

    springdoc.api-docs.enabled=true
    springdoc.api-docs.path=/v3/api-docs

    springdoc.swagger-ui.enabled=true
    springdoc.swagger-ui.config-url=/device/v3/api-docs/swagger-config
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.swagger-ui.url=/device/v3/api-docs

    springdoc.swagger-ui.disable-swagger-default-url=true

    spring.rabbitmq.host=synchronization-topic
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest

    topic.exchange.name=energy-management-exchange

    device.microservice.queue=device-microservice-queue
    device.routing.key=device-microservice.*

    monitoring.microservice.queue=monitoring-microservice-queue
    monitoring.routing.key=monitoring-microservice.*

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