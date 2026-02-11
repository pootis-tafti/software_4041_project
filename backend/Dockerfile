# Use a Maven image to build the application
FROM docker.arvancloud.ir/maven:3.9.9 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
COPY src ./src

# Compile the application
RUN mvn clean package -DskipTests

# Use a lightweight JRE image to run the application
FROM docker.arvancloud.ir/openjdk:21

# Set the working directory
WORKDIR /app

# Copy the compiled JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port (change if necessary)
EXPOSE 9192

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

