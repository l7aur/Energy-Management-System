FROM maven:3.9 AS build

LABEL authors="l7aur"

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /authentication-microservice/pom.xml
WORKDIR /authentication-microservice
RUN mvn dependency:go-offline
COPY ./src /authentication-microservice/src

WORKDIR /authentication-microservice
RUN mvn clean install -X -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /authentication-microservice
COPY --from=build /authentication-microservice/target/authentication-microservice-1.0.0.jar /authentication-microservice/authentication-microservice.jar
CMD ["java", "-jar", "authentication-microservice.jar"]