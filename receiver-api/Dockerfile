FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY receiver-api/gradlew receiver-api/gradlew.bat receiver-api/settings.gradle.kts receiver-api/build.gradle.kts ./
COPY receiver-api/gradle ./gradle

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

COPY receiver-api/src ./src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]