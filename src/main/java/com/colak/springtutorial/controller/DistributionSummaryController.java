package com.colak.springtutorial.controller;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(path = "/distsummary")
@RequiredArgsConstructor
public class DistributionSummaryController {

    private final Random random = ThreadLocalRandom.current();

    private DistributionSummary distributionSummary;

    @Autowired
    public void setRegistry(MeterRegistry registry) {
        distributionSummary = createDistributionSummary(registry);
    }

    // http://localhost:8080/distsummary/read
    // http://localhost:8080/actuator/prometheus

    // See these types
    // myDistributionSummary_count
    // myDistributionSummary_sum
    @GetMapping("/read")
    public String read() throws InterruptedException {
        // Imitating call latency
        long millis = 10 + random.nextLong(50);
        Thread.sleep(millis);

        distributionSummary.record(millis);
        return String.valueOf(millis);
    }

    private DistributionSummary createDistributionSummary(MeterRegistry meterRegistry) {
        // Count how many times this API has been called
        return DistributionSummary.builder("myDistributionSummary")
                .description("A custom distribution summary")
                .tag("region", "us-east")
                .register(meterRegistry);
    }
}
