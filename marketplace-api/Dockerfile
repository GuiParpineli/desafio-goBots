FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY ../gradlew settings.gradle.kts build.gradle.kts ./
COPY ../gradle ./gradle
COPY ../core ./core
COPY ../dataprovider ./dataprovider
COPY ../marketplace-api ./marketplace-api
RUN chmod +x gradlew && ./gradlew :marketplace-api:bootJar --no-daemon

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/marketplace-api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]