package com.colak.springactuatormicrometertutorial.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(path = "/timer")
@RequiredArgsConstructor
public class TimerController {

    private final Random random = ThreadLocalRandom.current();

    private Timer timer;

    @Autowired
    public void setRegistry(MeterRegistry registry) {
        timer = createTimer(registry);
    }


    // http://localhost:8080/timer/read
    // http://localhost:8080/actuator/prometheus

    // See these types
    // mytimer_seconds_count
    // mytimer_seconds_max
    @GetMapping("/read")
    public String read() {

        long millis = 10 + random.nextLong(50);

        timer.record(() -> {
            try {
                // Imitating call latency
                Thread.sleep(millis);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();

            }
        });
        return String.valueOf(millis);
    }

    private Timer createTimer(MeterRegistry meterRegistry) {
        return Timer.builder("mytimer")
                .description("Times something")
                .tag("region", "us-east")
                .register(meterRegistry);
    }
}
