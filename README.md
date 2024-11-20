# Read Me First

The original idea is from  
https://medium.com/@igorreshetnev/observing-rest-api-performance-with-springboot-prometheus-grafana-and-gatling-local-test-fee7ab393546

Another example is from  
https://altimetrikpoland.medium.com/spring-boot-micrometer-prometheus-and-grafana-how-to-add-custom-metrics-to-your-application-7f3a21a812f

# Node Exporter and Alert Manager

The original idea is from  
https://mxulises.medium.com/simple-prometheus-setup-on-docker-compose-f702d5f98579

# Actuator Prometheus

http://localhost:8080/actuator/prometheus

# Prometheus

http://localhost:9090

# PromQL

On prometheus see these metrics

# http_server_requests_seconds_count
Request count of given endpoint per minute

```
sum(rate(http_server_requests_seconds_count{uri=~".*api/v1/message/notification.*"}[1m])) * 60
sum(rate(http_server_requests_seconds_count{uri=~".*api/v1/message/mail.*"}[1m])) * 60
```

# System
system_cpu_usage
jvm_memory_used_bytes

# Others
spring_data_repository_invocations_seconds_count
spring_data_repository_invocations_seconds_count(method="findById")

# Grafana

http://localhost:3000

