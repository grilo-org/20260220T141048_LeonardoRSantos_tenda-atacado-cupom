FROM maven:3.9-eclipse-temurin-8 AS build
ARG ACCESS_TOKEN
ENV ACCESS_TOKEN=${ACCESS_TOKEN}

COPY . /src
WORKDIR /src

RUN mvn -DskipTests clean package -Dmaven.repo.local=.m2/repository

FROM eclipse-temurin:8-jre-alpine
VOLUME /tmp

COPY --from=build /src/target/atacado-cupom-service.jar /app.jar

EXPOSE 8084
ENV TZ="America/Sao_Paulo"

ENTRYPOINT ["java", "-jar", "/app.jar"]