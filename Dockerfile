# Build App
FROM openjdk:17.0.1-jdk-slim AS build
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean install -U -DskipTests

# Run App
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/coffe-shop-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]