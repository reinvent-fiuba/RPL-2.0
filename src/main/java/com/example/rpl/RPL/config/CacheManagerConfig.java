package com.example.rpl.RPL.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"prod"})
@Configuration
@EnableCaching
public class CacheManagerConfig {

    @Value("${app.cache.scoreboardTtl}")
    private Integer scoreboardTtl;

    @Value("${app.cache.submissionsCalendarTtl}")
    private Integer submissionsCalendarTtl;

    @Bean(name = "defaultCacheManager")
    public CacheManager cacheManager(Ticker ticker) {
        CaffeineCache scoreboard = buildCache("scoreboard", ticker, scoreboardTtl);
        CaffeineCache submissionsCalendar = buildCache("submissionsCalendar", ticker,
            submissionsCalendarTtl);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(scoreboard, submissionsCalendar));
        return manager;
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    private CaffeineCache buildCache(String name, Ticker ticker, int minutesToExpire) {
        return new CaffeineCache(name, Caffeine.newBuilder()
            .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
            .maximumSize(1000)
            .ticker(ticker)
            .build());
    }
}