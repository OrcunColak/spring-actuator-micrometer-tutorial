package com.colak.springtutorial.exception;

import com.colak.springtutorial.config.PrometheusCustomMonitor;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private PrometheusCustomMonitor monitor;

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public String handle(Exception exception) {
        monitor.getRequestErrorCount().increment();
        return "error, message: " + exception.getMessage();
    }
}
