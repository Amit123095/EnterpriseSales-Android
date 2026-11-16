
package com.amit.enterprisesales.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> healthCheck() {

        return Map.of(
                "status", "UP",
                "application", "EnterpriseSales Backend",
                "message", "Backend is running successfully"
        );
    }
}

/* -- response
{
  "status": "UP",
  "message": "Backend is running successfully",
  "application": "EnterpriseSales Backend"
}
* */