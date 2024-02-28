package com.colak.springactuatormicrometertutorial.controller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExceptionController {

    // http://localhost:8080/exception
    // http://localhost:8080/actuator/prometheus

    // See this type
    // requests_error_total
    @GetMapping("/exception")
    public String exception() {
        throw new RuntimeException("Exception to increment the counter ");
    }
}
