package com.example.popularityScorer.github;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDateTime;

@Configuration
public class RollingWindowThrottler {
    private final CircularFifoQueue<LocalDateTime> circularFifoBuffer;
    public static final Duration CYCLE_DURATION = Duration.ofSeconds(3600);

    public RollingWindowThrottler() {
        this.circularFifoBuffer = new CircularFifoQueue<>(10);
    }

    public RollingWindowThrottler(Integer callsPerHour) {
        this.circularFifoBuffer = new CircularFifoQueue<>(callsPerHour);
    }

    public synchronized void next() {

        try {
            LocalDateTime now = LocalDateTime.now();
            if (!circularFifoBuffer.isAtFullCapacity()) {
                circularFifoBuffer.add(now);
                return;
            }
            long between = Duration.between(circularFifoBuffer.peek(), now).toMillis();
            if (between <= CYCLE_DURATION.toMillis()) {
                Thread.sleep(
                        Math.max(
                                CYCLE_DURATION.toMillis() - between,
                                ((1 / circularFifoBuffer.maxSize()) * CYCLE_DURATION.toMillis())));
                circularFifoBuffer.add(now);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
