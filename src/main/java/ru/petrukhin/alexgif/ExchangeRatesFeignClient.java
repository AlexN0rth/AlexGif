package ru.petrukhin.alexgif;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.rate-service.name}", url = "${feign.rate-service.url}")
public interface ExchangeRatesFeignClient {
    @GetMapping(value = "/api/historical/2020-07-10.json")
    String getRates(@RequestParam("app_id") String appId, @RequestParam String base, @RequestParam String symbols);
}
