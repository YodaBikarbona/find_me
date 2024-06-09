# Use the official OpenJDK 21 image from the Docker Hub
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host machine to the container
COPY target/findme-0.0.1-SNAPSHOT.jar /app/findme-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "findme-0.0.1-SNAPSHOT.jar"]