FROM openjdk:25-ea-4-jdk-oraclelinux9

# Set working directory inside container
WORKDIR /app

# Copy the compiled jar to the container
COPY target/*.jar /app/app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]\
