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
        
        // Create tasks
        for (int i = 0; i < numberOfCalls; i++) {
            final int taskId = i;
            tasks.add(() -> {
                String threadName = Thread.currentThread().getName();
                log.info("Task {} started (Thread: {})", taskId, threadName);
                
                // Simulate API call (2-4 seconds random processing time)
                Thread.sleep((long) (2000 + Math.random() * 2000));
                
                log.info("Task {} completed (Thread: {})", taskId, threadName);
                return String.format("Task %d result (Thread: %s)", taskId, threadName);
            });
        }

        // Show thread pool state before execution
        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) executorService;
            log.info("Thread pool state before execution - Active: {}, Pool Size: {}, Queue Size: {}",
                executor.getActiveCount(),
                executor.getPoolSize(),
                executor.getQueue().size());
        }

        // Execute all tasks (timeout: 30 seconds)
        List<Future<String>> futures = executorService.invokeAll(tasks, 30, TimeUnit.SECONDS);
        
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
        
        // Show thread pool state after execution
        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) executorService;
            log.info("Thread pool state after execution - Active: {}, Pool Size: {}, Queue Size: {}, Completed: {}",
                executor.getActiveCount(),
                executor.getPoolSize(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount());
        }
        
        return results;
    }

    // Get thread pool statistics
    public String getThreadPoolStats() {
        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) executorService;
            return String.format(
                "Thread Pool Status [Active: %d, Pool Size: %d, Queue: %d, Completed: %d]",
                executor.getActiveCount(),
                executor.getPoolSize(),
                executor.getQueue().size(),
                executor.getCompletedTaskCount()
            );
        }
        return "Thread pool statistics not available";
    }
}
