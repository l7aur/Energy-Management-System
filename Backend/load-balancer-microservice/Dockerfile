FROM maven:3.9 AS build

LABEL authors="l7aur"

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /load-balancer-microservice/pom.xml
WORKDIR /load-balancer-microservice
RUN mvn dependency:go-offline
COPY ./src /load-balancer-microservice/src

WORKDIR /load-balancer-microservice
RUN mvn clean install -X -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /load-balancer-microservice
COPY --from=build /load-balancer-microservice/target/load-balancer-microservice-1.0.0.jar /load-balancer-microservice/load-balancer-microservice.jar
CMD ["java", "-jar", "load-balancer-microservice.jar"]