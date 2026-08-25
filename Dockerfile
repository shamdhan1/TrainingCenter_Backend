# 1. Build Phase
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy Maven descriptor and source tree
COPY pom.xml .
COPY src ./src
COPY frontend ./frontend

# Run packaging (Executes npm install, build, and bundles assets)
RUN mvn package -DskipTests

# 2. Execution Phase
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
