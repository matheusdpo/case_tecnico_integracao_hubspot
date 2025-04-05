package br.com.meetime.hubspot.v1.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    @Value("${hubspot.limit.days}")
    private long daysLimit;

    @Value("${hubspot.limit.seconds}")
    private long secondsLimit;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, k -> newBucket());
    }

    private Bucket newBucket() {
        Bandwidth shortTermLimit = Bandwidth.classic(secondsLimit,
                Refill.intervally(secondsLimit, Duration.ofSeconds(10)));

        Bandwidth longTermLimit = Bandwidth.classic(daysLimit,
                Refill.intervally(daysLimit, Duration.ofDays(1)));

        return Bucket.builder()
                .addLimit(shortTermLimit)
                .addLimit(longTermLimit)
                .build();
    }

    public boolean tryConsume(String key) {
        Bucket bucket = resolveBucket(key);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        return probe.isConsumed();
    }

}