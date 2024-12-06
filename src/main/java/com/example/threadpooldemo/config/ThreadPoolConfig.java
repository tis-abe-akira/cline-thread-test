package com.example.threadpooldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ThreadPoolConfig {
    
    @Bean(name = "fixedThreadPool")
    public ExecutorService executorService() {
        // システムで利用可能なプロセッサ数に基づいてスレッド数を設定
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        log.info("スレッドプールを初期化します。スレッド数: {}", threadPoolSize);
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
