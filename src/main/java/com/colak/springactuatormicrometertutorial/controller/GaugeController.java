package com.colak.springactuatormicrometertutorial.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(path = "/gauge")

@RequiredArgsConstructor
public class GaugeController {

    private final Random random = new Random();

    private Gauge gauge;

    private final AtomicInteger gaugeInteger = new AtomicInteger();

    @Autowired
    public void setRegistry(MeterRegistry registry) {
        gauge = createGauge(registry);
    }


    // http://localhost:8080/gauge/read
    // http://localhost:8080/actuator/prometheus

    // See these types
    // gauge_read_get
    // method_timed_seconds histogram
    @GetMapping("/read")
    // Measure how long it takes to serve this request
    @Timed(histogram = true, percentiles = {0.5, 0.75, 0.95, 0.99, 0.9999})
    public String read() throws InterruptedException {
        // Imitating call latency
        long millis = 10 + random.nextLong(50);
        Thread.sleep(millis);

        gaugeInteger.set((int) millis);

        return String.valueOf(millis);
    }

    private Gauge createGauge(MeterRegistry meterRegistry) {
        return Gauge.builder("gauge_read_get", gaugeInteger, AtomicInteger::get)
                .description("Gauges something")
                .tag("region", "us-east")
                .register(meterRegistry);
    }
}
