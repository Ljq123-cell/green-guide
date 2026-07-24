FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/green-guide-server-1.0.0.jar app.jar
EXPOSE 8686
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
