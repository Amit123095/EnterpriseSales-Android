
package com.amit.enterprisesales.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DatabaseHealthController {

    private final DataSource dataSource;

    public DatabaseHealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/db-health")
    public ResponseEntity<Map<String, String>> databaseHealth() {

        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet =
                        statement.executeQuery("SELECT current_database()")
        ) {

            resultSet.next();

            String databaseName = resultSet.getString(1);

            return ResponseEntity.ok(
                    Map.of(
                            "status", "UP",
                            "database", databaseName,
                            "message", "PostgreSQL connection successful"
                    )
            );

        } catch (SQLException exception) {

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(
                            Map.of(
                                    "status", "DOWN",
                                    "message", "Database connection failed"
                            )
                    );
        }
    }
}
