package com.example.threadpooldemo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.threadpooldemo.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ThreadDemoController {

    private final ExternalApiService externalApiService;

    @GetMapping("/parallel-calls")
    public ResponseEntity<?> executeParallelCalls(@RequestParam(defaultValue = "5") int numberOfCalls) {
        try {
            List<String> results = externalApiService.performParallelApiCalls(numberOfCalls);
            return ResponseEntity.ok()
                .body(new ParallelCallResponse(
                    results,
                    externalApiService.getThreadPoolStats()
                ));
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.internalServerError()
                .body("Error executing parallel calls: " + e.getMessage());
        }
    }

    @GetMapping("/thread-pool-stats")
    public ResponseEntity<String> getThreadPoolStats() {
        return ResponseEntity.ok(externalApiService.getThreadPoolStats());
    }

    // Response wrapper class
    private static class ParallelCallResponse {
        private final List<String> results;
        private final String threadPoolStats;

        public ParallelCallResponse(List<String> results, String threadPoolStats) {
            this.results = results;
            this.threadPoolStats = threadPoolStats;
        }

        public List<String> getResults() {
            return results;
        }

        public String getThreadPoolStats() {
            return threadPoolStats;
        }
    }
}
