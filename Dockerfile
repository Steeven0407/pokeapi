FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/pokeapi-0.0.1-SNAPSHOT.jar /app/pokeapi.jar

EXPOSE 8080

CMD [ "java", "-jar", "pokeapi.jar" ]