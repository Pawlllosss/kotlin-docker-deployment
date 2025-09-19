FROM amazoncorretto:21-alpine3.22

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/hackyeah2025-0.0.1-SNAPSHOT.jar /app/kotlin-app.jar

EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/kotlin-app.jar"]