# User Microservice — Spring Boot

The User microservice is part of the **Energy Management application**.  
Its responsibility is to store and manage **user-related information such as email, phone number, address, and other profile details**.

This service is **not publicly exposed** and is **accessible only through the API Gateway**.  
It runs as part of the **Docker Compose microservices environment**.

## Features

- Spring Security-based role identification
- Role-based access control (RBAC)
- PostgreSQL user storage
- Springdoc / OpenAPI documentation: `http://localhost/user/public/swagger-ui/index.html`
- CRUD operations for user entities
- Docker container support
- JWT token is validated by traefik API-gateway
- RabbitMQ-based synchronization to ensure database consistency between this microservice, the `authentication-microservice`, and the `device-microservice`

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
- Springdoc endpoints are prefixed with: `/user/public`
- Endpoints are prefixed with: `/user/secured`

## Architecture

This service interacts with:

- **User Database** - separate docker network: `user-network`
- **API Gateway (Traefik)** — all requests routed & authenticated externally: `traefik-network`
- **RabbitMQ `energy-management-exchange` topic** - ensures partial consistency between this microservice, the `authentication-microservice`, and the `device-microservice` (separate docker network: `synchronization-topic-network`)

No direct public communication is allowed — internal network only.

The microservice interacts with the `authentication-microservice` by means of a RabbitMQ queue (subscriber): `user-microservice-queue` that is part of the `energy-management-exchange` topic. This assures partial consistency between the `authentication-microservice-database` and the `user-microservice-database`.

The microservice interacts with the `device-microservice` by means of a RabbitMQ queue (publisher): `device-microservice-queue` that is part of the `energy-management-exchange` topic. This assures partial consistency between the `device-microservice-database` and the `user-microservice-database`.

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

Particularly, the `authentication-microservice` handles and dispatches the following types of messages: `USER_CREATE`, `USER_UPDATE`, `USER_DELETE`. 

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

## application.properties requirements

    spring.application.name=user-microservice
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
    springdoc.swagger-ui.config-url=/user/v3/api-docs/swagger-config
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.swagger-ui.url=/user/v3/api-docs

    springdoc.swagger-ui.disable-swagger-default-url=true

    spring.rabbitmq.host=synchronization-topic
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest

    topic.exchange.name=energy-management-exchange

    user.microservice.queue=user-microservice-queue
    user.routing.key=user-microservice.*

    device.microservice.queue=device-microservice-queue
    device.routing.key=device-microservice.*

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