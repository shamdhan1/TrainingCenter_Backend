# 1. Build Phase
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy Maven descriptor and source tree
COPY backend/pom.xml ./backend/
COPY backend/src ./backend/src
COPY frontend ./frontend

# Run packaging (Executes npm install, build, and bundles assets)
RUN mvn -f backend/pom.xml package -DskipTests

# 2. Execution Phase
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/backend/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
