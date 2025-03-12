FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ms-event-manager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080do
CMD ["java", "-jar", "app.jar"]