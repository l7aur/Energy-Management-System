# Customer Support Microservice - Spring Boot

The customer support microservice is part of the **Energy Management application**.  

## Features

- Spring Security-based role identification
- Role-based access control (RBAC)
- Springdoc / OpenAPI documentation: `http://localhost/customersupport/public/swagger-ui/index.html`
- Docker container support
- JWT token is validated by the Traefik API-gateway

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

## Security Notes

- JWT-based authentication
- No session state (stateless microservice)
- Gateway provides request authentication boundary
- Springdoc endpoints are prefixed with: `/customersupport/public`
- Endpoints are prefixed with: `/customersupport/secured`
- The web socket connection is not secured

## Architecture

This service interacts with:

- **API Gateway (Traefik)** — all requests routed & authenticated externally: `traefik-network`.
- The **web socket** connection bypasses Traefik and can be accessed at: `ws://localhost/customersupport/public/ws`.
- This microservice interacts with Google's free LLM over SpringAI.
- Stores up to 100 messages in an in-memory cache (list).

## application.properties requirements

    spring.application.name=customer-support-microservice
    server.port=7777

    springdoc.api-docs.enabled=true
    springdoc.api-docs.path=/v3/api-docs

    springdoc.swagger-ui.enabled=true
    springdoc.swagger-ui.config-url=/customersupport/public/v3/api-docs/swagger-config
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.swagger-ui.url=/customersupport/public/v3/api-docs

    springdoc.swagger-ui.disable-swagger-default-url=true

    spring.ai.google.genai.api-key=<your-very-private-google-gemini-api-key>

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