FROM openjdk:8-jdk-alpine AS builder
COPY . /usr/src/stores-finder-backend
WORKDIR /usr/src/stores-finder-backend
RUN ./gradlew clean build --exclude-task test

FROM openjdk:8-jdk-alpine
COPY --from=builder /usr/src/stores-finder-backend/build/libs/stores-finder-backend-0.0.1-SNAPSHOT.jar /usr/src/stores-finder-backend/
WORKDIR /usr/src/stores-finder-backend
EXPOSE 8080
CMD ["java", "-jar", "stores-finder-backend-0.0.1-SNAPSHOT.jar"]
