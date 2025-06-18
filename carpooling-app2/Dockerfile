FROM eclipse-temurin:17-jre as runtime
WORKDIR /app
COPY target/carpooling-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]