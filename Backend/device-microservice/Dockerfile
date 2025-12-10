FROM maven:3.9 AS build

LABEL authors="l7aur"

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /device-microservice/pom.xml
WORKDIR /device-microservice
RUN mvn dependency:go-offline
COPY ./src /device-microservice/src

WORKDIR /device-microservice
RUN mvn clean install -X -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /device-microservice
COPY --from=build /device-microservice/target/device-microservice-1.0.0.jar /device-microservice/device-microservice.jar
CMD ["java", "-jar", "device-microservice.jar"]