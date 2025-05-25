# Build stage
FROM maven:3.9.9-ibm-semeru-21-jammy AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:latest AS runtime
WORKDIR /app
COPY --from=build /app/target/banking-app.jar app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]