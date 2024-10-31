# Step 1: Use an official Java 21 runtime as the base image
FROM openjdk:21-jdk-slim

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file into the container
# Replace 'your-app.jar' with the actual name of your JAR file
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the port your application will run on
# Replace '8080' with the actual port your Spring Boot app uses
EXPOSE 8082

# Step 5: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
