FROM maven:3.9 AS build

LABEL authors="l7aur"

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /notification-microservice/pom.xml
WORKDIR /notification-microservice
RUN mvn dependency:go-offline
COPY ./src /notification-microservice/src

WORKDIR /notification-microservice
RUN mvn clean install -X -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /notification-microservice
COPY --from=build /notification-microservice/target/notification-microservice-1.0.0.jar /notification-microservice/notification-microservice.jar
CMD ["java", "-jar", "notification-microservice.jar"]