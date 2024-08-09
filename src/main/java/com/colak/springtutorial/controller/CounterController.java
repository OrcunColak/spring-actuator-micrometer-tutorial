package com.colak.springtutorial.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(path = "/counter")
@RequiredArgsConstructor
public class CounterController {

    private final Random random = ThreadLocalRandom.current();

    private Counter counter;

    @Autowired
    public void setRegistry(MeterRegistry registry) {
        counter = createCounter(registry);
    }


    // http://localhost:8080/counter/read
    // http://localhost:8080/actuator/prometheus

    // See these types
    // counter_read_get_total
    // method_timed_seconds histogram
    @GetMapping("/read")
    // Measure how long it takes to serve this request
    @Timed(histogram = true, percentiles = {0.5, 0.75, 0.95, 0.99, 0.9999})
    public String read() throws InterruptedException {
        // Imitating call latency
        Thread.sleep(10 + random.nextLong(50));

        counter.increment();
        return String.valueOf(counter.count());
    }

    private Counter createCounter(MeterRegistry meterRegistry) {
        // Count how many times this API has been called
        return Counter.builder("counter_read_get")
                .description("The number of requests to /read endpoint")
                .tag("region", "us-east")
                .register(meterRegistry);
    }
}
