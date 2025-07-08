FROM azul/zulu-openjdk-alpine:21-latest
WORKDIR /app
COPY . .
RUN ./gradlew build -x test
EXPOSE 8080
CMD ["java", "-jar", "build/libs/spotify-app-0.0.1-SNAPSHOT.jar"]
