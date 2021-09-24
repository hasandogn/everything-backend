FROM openjdk:11 AS build

COPY . .
RUN chmod +x gradlew
RUN ./gradlew build

FROM openjdk:11
WORKDIR demo
#ADD src/main/resources/application.properties application.properties
COPY --from=build ./build/libs/*.jar ./demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]

#FROM openjdk:11
#VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} demo.jar
#ENTRYPOINT ["java", "-jar", "app/jar"]
