FROM maven:3.9 AS build

LABEL authors="l7aur"

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /user-microservice/pom.xml
WORKDIR /user-microservice
RUN mvn dependency:go-offline
COPY ./src /user-microservice/src

WORKDIR /user-microservice
RUN mvn clean install -X -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /user-microservice
COPY --from=build /user-microservice/target/user-microservice-1.0.0.jar /user-microservice/user-microservice.jar
CMD ["java", "-jar", "user-microservice.jar"]