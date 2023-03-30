FROM maven:3.6.3-jdk-11-slim AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn package spring-boot:repackage -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.4_11-alpine
RUN mkdir /app
COPY --from=build /project/target/*.jar /app/java-application.jar
WORKDIR /app
CMD "java" "-jar" "java-application.jar"