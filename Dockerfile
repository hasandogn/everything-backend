FROM openjdk:11 AS build

COPY . .
RUN chmod +x gradle
RUN ./gradlew build

FROM openjdk:11
WORKDIR demo
COPY --from=build build/libs/*.jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]