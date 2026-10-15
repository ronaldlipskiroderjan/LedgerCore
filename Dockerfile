# syntax=docker/dockerfile:1.7

FROM maven:3.9.16-eclipse-temurin-21-alpine AS build

WORKDIR /workspace

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn --batch-mode --no-transfer-progress clean package -DskipTests && \
    cp target/LedgerCore-*.jar /workspace/app.jar

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S ledgercore && \
    adduser -S -G ledgercore ledgercore

WORKDIR /app

COPY --from=build --chown=ledgercore:ledgercore /workspace/app.jar ./app.jar

USER ledgercore

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
