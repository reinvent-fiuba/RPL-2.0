# Use the official Gradle image to build the project
FROM gradle:8.2.1-jdk17 AS build

# Set the working directory in the container
WORKDIR /home/gradle/project

# Copy the Gradle wrapper and project files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src


# Make the Gradle wrapper executable
RUN chmod +x gradlew

# Build the project
RUN ./gradlew build

# Use the official OpenJDK 17 image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Set environment variables with default values
ENV SPRING_PROFILES_ACTIVE=default
ENV QUEUE_SERVICE_HOST=localhost

# Expose the port the app runs on
EXPOSE 8080


# FROM azul/zulu-openjdk-alpine:17
# VOLUME /tmp
# ARG JAR_FILE="./build/libs/RPL-0.0.1-SNAPSHOT.jar"
# COPY ${JAR_FILE} app.jar

COPY "./newrelic/newrelic.jar" "/tmp"
COPY "./newrelic/newrelic.yml" "/tmp"

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-javaagent:/tmp/newrelic.jar", "-jar", "app.jar"]
