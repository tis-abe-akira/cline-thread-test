package com.example.threadpooldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {
    
    @Bean(name = "fixedThreadPool")
    public ExecutorService executorService() {
        // Using number of available processors as the thread pool size
        // This is just an example - in production, you'd want to tune this based on your specific needs
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
