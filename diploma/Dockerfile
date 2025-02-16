FROM maven:3-eclipse-temurin-22-alpine AS build
WORKDIR /usr/app
ADD . /usr/app
RUN mvn clean package

FROM eclipse-temurin:22-alpine
WORKDIR /app
COPY --from=build /usr/app/target/*.jar /app/runner.jar
EXPOSE 9000
CMD ["java", "-jar", "runner.jar"]