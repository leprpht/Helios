FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x gradlew
COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/helios-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]