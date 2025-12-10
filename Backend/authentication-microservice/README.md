# Authentication Microservice — Spring Boot

The authentication microservice is part of the **Energy Management application**.  
Its responsibility is to securely authenticate users and manage system access, including generating JWT tokens, managing roles/authorities, and storing encrypted credentials.

This service is **accessible only through the API Gateway** and is included in the shared **Docker Compose** environment.

## Features

- Spring Security-based authentication
- Auth0 JWT token generation & validation
- Secure password encryption
- Role-based access control (RBAC)
- PostgreSQL user and token storage
- CRUD operations for authentication entities
- Springdoc / OpenAPI documentation: `http://localhost/auth/public/swagger-ui/index.html`
- Docker container support
- Configurable token expiration policies
- JWT token is validated by traefik API-gateway
- RabbitMQ-based synchronization to ensure database consistency between this microservice and the `user-microservice`

## Tech Stack

| Component      | Technology                   |
|----------------|------------------------------|
| Language       | Java 21                      |
| Framework      | Spring Boot                  |
| Build Tool     | Maven                        |
| Database       | PostgreSQL                   |
| Security       | Spring Security, Auth0 JWT   |
| Documentation  | Springdoc OpenAPI            |
| Container      | Docker                       |
| Deployment     | Docker Compose               |
| API-gateway    | Traefik + JWT & CORS plugins |
| Message broker | RabbitMQ                     |

## Security Notes

- Passwords stored encrypted using BCrypt
- JWT-based authentication
- Role/authority mapping stored in DB
- No session state (stateless microservice)
- Gateway provides request authentication boundary
- Springdoc endpoints are prefixed with: `/auth/public`
- `/auth/secured/login` and `/auth/secured/register` are public (unsecured) endpoints
- Endpoints are prefixed with: `/auth/secured`

## Architecture

This service interacts with:

- **Authentication Database** - separate docker network: `authentication-network`
- **API Gateway (Traefik)** - all requests routed & authenticated externally: `traefik-network`
- **RabbitMQ `energy-management-exchange` topic** - ensures partial consistency between this microservice and the `user-microservice` (separate docker network: `synchronization-topic-network`)

The microservice interacts with the `user-microservice` by means of a RabbitMQ queue (publisher): `user-microservice-queue` that is part of the `energy-management-exchange` topic. This assures partial consistency between the `authentication-microservice-database` and the `user-microservice-database`.

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

Particularly, the `authentication-microservice` publishes/dispatches the following types of messages: `USER_CREATE`, `USER_UPDATE`, `USER_DELETE`. It does not react to any kind of messages (it is not a subscriber of any queue). 

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

    spring.application.name=authentication-microservice
    server.port=your-server-port

    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.datasource.url=docker-database-endpoint
    spring.datasource.username=database-username
    spring.datasource.password=database-password

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.open-in-view=false

    jwt.expiration=1h
    jwt.secret=your-very-secret

    springdoc.api-docs.enabled=true
    springdoc.api-docs.path=/v3/api-docs

    springdoc.swagger-ui.enabled=true
    springdoc.swagger-ui.config-url=/auth/v3/api-docs/swagger-config
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.swagger-ui.url=/auth/v3/api-docs

    springdoc.swagger-ui.disable-swagger-default-url=true

    spring.rabbitmq.host=synchronization-topic
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest

    topic.exchange.name=energy-management-exchange

    user.microservice.queue=user-microservice-queue
    user.routing.key=user-microservice.*

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