# Monitoring microservice

The Monitoring microservice is part of the **Energy Management application**.  

This service is **not publicly exposed** and is **accessible only through the API Gateway**.  
It runs as part of the **Docker Compose microservices environment**.

## Features

- Spring Security-based role identification
- Role-based access control (RBAC)
- PostgreSQL user and device storage (data duplication)
- User-Device relationships using data duplication
- CRUD operations for device entities
- Springdoc / OpenAPI documentation: `http://localhost/monitoring/public/swagger-ui/index.html`
- Docker container support
- JWT token is validated by traefik API-gateway
- RabbitMQ-based synchronization to ensure database consistency between this microservice and the `device-microservice`
- This service supports replication (however, changes are required to the `load-balancer-microservice`)

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
- Springdoc endpoints are prefixed with: `/monitoring/public`
- Endpoints are prefixed with: `/monitoring/secured`

## Architecture

This service interacts with:

- **Monitoring Database** - separate docker network: `monitoring-network`, the database is shared between all replicas
- **API Gateway (Traefik)** — all requests routed & authenticated externally: `traefik-network`
- **RabbitMQ `energy-management-exchange` topic** - ensures partial consistency between this microservice and the `device-microservice` (separate docker network: `synchronization-topic-network`)
- **RabbitMQ `ingestion-exchange`** - integrated in `ingestion-network` provides queues named `ingestion.queue.#` where sensor data is rerouted by the `load-balancer-microservice`. A message has the following form:

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

The microservice interacts with the RabbitMQ `ingestion.queue.#` as a subscriber. The number of messages per queue is "balanced" by the `load-balancer-microservice` based on an internal heuristic.

The microservice interacts with the `device-microservice` by means of a RabbitMQ queue (subscriber): `monitoring-microservice-queue` that is part of the `energy-management-exchange` topic. This assures partial consistency between the `device-microservice-database`. Messages coming over this queue can be processed by any of the replicas as the `monitoring-microservice-database` is shared between them. 

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

Particularly, the `monitoring-microservice` handles the following types of messages: `DEVICE_CREATE`, `DEVICE_DELETE`. `DEVICE_UPDATE` is not required as only the id of the device from de `device-microservice-database` is referenced in the `monitoring-microservice-database`. 

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

    spring.application.name=monitoring-microservice
    server.port=7777

    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.datasource.url=jdbc:postgresql://monitoring-microservice-database:5432/monitoring_microservice_database
    spring.datasource.username=DS_l7aur_monitoring_microservice
    spring.datasource.password=2003

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.open-in-view=false

    springdoc.api-docs.enabled=true
    springdoc.api-docs.path=/v3/api-docs

    springdoc.swagger-ui.enabled=true
    springdoc.swagger-ui.config-url=/monitoring/public/v3/api-docs/swagger-config
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.swagger-ui.url=/monitoring/public/v3/api-docs

    springdoc.swagger-ui.disable-swagger-default-url=true

    ingestion.queue.name=ingestion-queue
    ingestion.routing.key=ingestion.queue.#
    ingestion.exchange.name=ingestion-exchange
    spring.rabbitmq.host=ingestion-topic
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest

    spring.rabbitmq.host2=synchronization-topic
    spring.rabbitmq.port2=5672
    spring.rabbitmq.username2=guest
    spring.rabbitmq.password2=guest

    topic.exchange.name=energy-management-exchange

    monitoring.microservice.queue=monitoring-microservice-queue
    monitoring.routing.key=monitoring-microservice.*

    notification.microservice.queue=notification-microservice-queue
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