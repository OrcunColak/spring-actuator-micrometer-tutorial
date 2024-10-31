package com.colak.springtutorial.controller;

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

    // Prometheus uses seconds as its base unit for time. In real life scenarios, however, you will often have to measure and track very small,
    // sub-second time durations. This means that you will have to find a way to track these small numbers (< 1.0 seconds) in the histogram.
    // Using a scaled DistributionSummary does not work well to solve this issue because the unit of the metrics published will be scaled as well.
    // For example, if you use a scale of 1000, the time measurement published will be in microseconds instead of seconds, which may cause confusion later.
    // To solve this problem, instead of DistributionSummary, you can use Micrometer Timer.
    // This class keeps track of the time measurements in nanoseconds and takes care of the conversion to seconds before publishing them.
    private Timer createTimer(MeterRegistry meterRegistry) {
        return Timer.builder("mytimer")
                .description("Times something")
                .tag("region", "us-east")
                .register(meterRegistry);
    }
}
