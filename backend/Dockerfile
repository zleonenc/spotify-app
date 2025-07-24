# Stage 1: Build the application using a JDK image
FROM azul/zulu-openjdk-alpine:21-latest AS builder

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle gradle

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || return 0

COPY src src
RUN ./gradlew build -x test --no-daemon

# Stage 2: Create the final, lean image using a JRE image
FROM azul/zulu-openjdk-alpine:21-jre-latest

WORKDIR /app

COPY --from=builder /app/build/libs/spotify-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]