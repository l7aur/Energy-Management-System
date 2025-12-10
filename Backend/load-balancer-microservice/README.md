# Load Balancer Microservice - Spring Boot

The load balancer microservice is part of the **Energy Management application**.

## Features

- Spring Security-based role identification
- Role-based access control (RBAC)
- Springdoc / OpenAPI documentation: `http://localhost/loadbalancer/public/swagger-ui/index.html`
- Docker container support
- JWT token is validated by Traefik API-gateway
- Uses an id modulo number of replicas heuristic to balance the number of requests between multiple `monitoring-microservice` replicas

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
- Springdoc endpoints are prefixed with: `/loadbalancer/public`
- Endpoints are prefixed with: `/loadbalancer/secured`

## Architecture

This service interacts with:

- **API Gateway (Traefik)** — all requests routed & authenticated externally: `traefik-network`. Currently, no endpoint is available.
- **RabbitMQ `ingestion-exchange` topic** - ensures messages coming from outside are rerouted to one of the available `ingestion.queue.#` based on the `id % number of replicas` heuristic.
- **RabbitMQ `sensor-data-queue`** - integrated in `data-network`, provides an endpoint (`localhost:5672`) where sensors can publish power consumption recordings of the form:

```json
    {
    "timestamp" : 
    {
        "hour" : <number:uint>,
        "minute" : <number:uint>,
        "second" : <number:uint>
    },
    "deviceId" : <number:uint>,
    "measuredValue" : <number:float> // always positive
    }
```

The microservice interacts with the `sensor-data-queue` as a subscriber. The queue has a public, open-access endpoint where sensors/emulators can place power consumption recordings that are going to be rerouted over corresponding `ingestion.queue.#` to the `monitoring-microservice`.

The number of replicas is hardcoded to `3` and must be reflected in the `docker-compose.yml` file. Additionally, following the naming convention for the queues is strongly advised. 

The microservice interacts with the `monitoring-microservice` replicas by means of RabbitMQ queues (publisher): `ingestion.queue.#` that are part of the `ingestion-exchange` topic.

## application.properties requirements

    spring.application.name=load-balancer-microservice
    server.port=7777
    
    springdoc.api-docs.enabled=true
    springdoc.api-docs.path=/v3/api-docs
    
    springdoc.swagger-ui.enabled=true
    springdoc.swagger-ui.config-url=/loadbalancer/public/v3/api-docs/swagger-config
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.swagger-ui.url=/loadbalancer/public/v3/api-docs
    
    springdoc.swagger-ui.disable-swagger-default-url=true
    
    sensor.data.queue.name=sensor-data-queue
    spring.rabbitmq.host=sensor-data-queue
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest
    
    ingestion.exchange.name=ingestion-exchange
    ingestion.microservice.queue.prefix=ingestion-queue
    spring.rabbitmq.ingestion.host=ingestion-topic
    spring.rabbitmq.ingestion.port=5672
    spring.rabbitmq.ingestion.username=guest
    spring.rabbitmq.ingestion.password=guest

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