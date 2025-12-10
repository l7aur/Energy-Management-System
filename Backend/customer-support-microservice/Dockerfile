FROM maven:3.9 AS build

LABEL authors="l7aur"

ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV CATALINA_OPTS="-Duser.timezone=UTC"

COPY ./pom.xml /customer-support-microservice/pom.xml
WORKDIR /customer-support-microservice
RUN mvn dependency:go-offline
COPY ./src /customer-support-microservice/src

WORKDIR /customer-support-microservice
RUN mvn clean install -X -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /customer-support-microservice
COPY --from=build /customer-support-microservice/target/customer-support-microservice-1.0.0.jar /customer-support-microservice/customer-support-microservice.jar
CMD ["java", "-jar", "customer-support-microservice.jar"]