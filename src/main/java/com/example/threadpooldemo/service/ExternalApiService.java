package com.example.threadpooldemo.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalApiService {

    private final ExecutorService executorService;

    public ExternalApiService(@Qualifier("fixedThreadPool") ExecutorService executorService) {
        this.executorService = executorService;
    }

    public List<String> performParallelApiCalls(int numberOfCalls) throws InterruptedException, ExecutionException {
        List<Callable<String>> tasks = new ArrayList<>();
        
        // Create tasks that simulate API calls
        for (int i = 0; i < numberOfCalls; i++) {
            final int taskId = i;
            tasks.add(() -> {
                // Simulate API call with random processing time
                Thread.sleep((long) (Math.random() * 2000));
                String threadName = Thread.currentThread().getName();
                log.info("Task {} executed by thread: {}", taskId, threadName);
                return String.format("Result from task %d (Thread: %s)", taskId, threadName);
            });
        }

        // Execute all tasks in parallel with a timeout
        List<Future<String>> futures = executorService.invokeAll(tasks, 5, TimeUnit.SECONDS);
        
        // Collect results
        List<String> results = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                results.add(future.get(1, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                log.error("Task timed out", e);
                results.add("Task timed out");
            }
        }
        
        return results;
    }

    // Method to get current thread pool statistics
    public String getThreadPoolStats() {
        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) executorService;
            return String.format(
                "Thread Pool Stats: [Active: %d, Pool: %d, Queue: %d, Completed: %d]",
                executor.getActiveCount(),
                executor.getPoolSize(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount()
            );
        }
        return "Thread pool statistics not available";
    }
}
