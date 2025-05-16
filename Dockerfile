# Build stage
FROM maven:3.9.9-ibm-semeru-21-jammy AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
EXPOSE 8082
CMD ["java", "-jar", "target/app.jar"]