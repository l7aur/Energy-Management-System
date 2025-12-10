FROM maven:3.9 AS build

LABEL authors="l7aur"

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /monitoring-microservice/pom.xml
WORKDIR /monitoring-microservice
RUN mvn dependency:go-offline
COPY ./src /monitoring-microservice/src

WORKDIR /monitoring-microservice
RUN mvn clean install -X -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /monitoring-microservice
COPY --from=build /monitoring-microservice/target/monitoring-microservice-1.0.0.jar /monitoring-microservice/monitoring-microservice.jar
CMD ["java", "-jar", "monitoring-microservice.jar"]