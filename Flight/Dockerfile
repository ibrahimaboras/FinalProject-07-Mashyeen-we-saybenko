FROM openjdk:25-ea-4-jdk-oraclelinux9

WORKDIR /app

COPY target/*.jar /target/app.jar

ENTRYPOINT ["java", "-jar", "/target/app.jar"]