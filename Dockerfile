FROM maven:3.9.7-amazoncorretto-21 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app

RUN mvn clean install

FROM amazoncorretto:21-alpine-jdk

COPY --from=build /app/target/cycle-care-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]