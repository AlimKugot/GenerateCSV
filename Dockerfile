FROM eclipse-temurin:11-jdk-alpine as builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install


FROM eclipse-temurin:11-jre-alpine
WORKDIR /opt/app
COPY --from=builder /opt/app/target/GenerateCSV.jar /opt/app/GenerateCSV.jar
ENTRYPOINT ["java", "-jar", "/opt/app/GenerateCSV.jar"]