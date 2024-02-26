package com.colak.springactuatormicrometertutorial.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
public class ReadController {

    private final MeterRegistry meterRegistry;
    private final Random random = new Random();

    // http://localhost:8080/read
    // http://localhost:8080/actuator/prometheus
    // See these types
    // api_read_get_total
    // method_timed_seconds histogram
    @GetMapping("/read")
    @Timed(histogram = true, percentiles = {0.5, 0.75, 0.95, 0.99, 0.9999})
    public String read() throws InterruptedException {
        Counter counter = Counter.builder("api_read_get")
                .description("a number of requests to /read endpoint")
                .register(meterRegistry);

        // Imitating call latency
        Thread.sleep(10 + random.nextLong(50));
        counter.increment();
        return "Done";
    }
}
