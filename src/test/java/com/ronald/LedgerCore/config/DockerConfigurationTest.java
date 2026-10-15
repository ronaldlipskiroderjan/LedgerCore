package com.ronald.LedgerCore.config;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DockerConfigurationTest {

    @Test
    void composeShouldDefineApplicationAndHealthyPostgresDependency() throws IOException {
        String compose = readProjectFile("compose.yaml");
        Map<?, ?> document = assertInstanceOf(Map.class, new Yaml().load(compose));
        Map<?, ?> services = assertInstanceOf(Map.class, document.get("services"));

        assertAll(
                () -> assertTrue(services.containsKey("app")),
                () -> assertTrue(services.containsKey("postgres")),
                () -> assertTrue(compose.contains("postgres:18.6-alpine3.24")),
                () -> assertTrue(compose.contains("pg_isready")),
                () -> assertTrue(compose.contains("condition: service_healthy")),
                () -> assertTrue(compose.contains("jdbc:postgresql://postgres:5432/")),
                () -> assertTrue(compose.contains("postgres-data:/var/lib/postgresql")),
                () -> assertTrue(compose.contains("SPRING_DATASOURCE_USERNAME")),
                () -> assertTrue(compose.contains("JWT_KEY"))
        );
    }

    @Test
    void dockerfileShouldUseJava21MultiStageBuildAndNonRootRuntime() throws IOException {
        String dockerfile = readProjectFile("Dockerfile");

        assertAll(
                () -> assertTrue(dockerfile.contains("maven:3.9.16-eclipse-temurin-21-alpine AS build")),
                () -> assertTrue(dockerfile.contains("FROM eclipse-temurin:21-jre-alpine")),
                () -> assertTrue(dockerfile.contains("COPY --from=build")),
                () -> assertTrue(dockerfile.contains("USER ledgercore")),
                () -> assertTrue(dockerfile.contains("EXPOSE 8080")),
                () -> assertTrue(dockerfile.contains("ENTRYPOINT [\"java\""))
        );
    }

    @Test
    void applicationConfigurationShouldUseExternalizedRuntimeProperties() throws IOException {
        String application = readProjectFile("src/main/resources/application.yaml");
        String environmentExample = readProjectFile(".env.example");

        assertAll(
                () -> assertTrue(application.contains("${SPRING_DATASOURCE_URL:")),
                () -> assertTrue(application.contains("${SPRING_DATASOURCE_USERNAME:")),
                () -> assertTrue(application.contains("${SPRING_DATASOURCE_PASSWORD:")),
                () -> assertTrue(application.contains("${JWT_KEY:")),
                () -> assertTrue(environmentExample.contains("POSTGRES_PASSWORD=")),
                () -> assertTrue(environmentExample.contains("JWT_KEY="))
        );
    }

    private String readProjectFile(String relativePath) throws IOException {
        return Files.readString(Path.of(relativePath));
    }
}
