FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY ../gradlew settings.gradle.kts build.gradle.kts ./
COPY ../gradle ./gradle
COPY ../receiver-api ./receiver-api
RUN chmod +x gradlew && ./gradlew :receiver-api:bootJar --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/receiver-api/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]