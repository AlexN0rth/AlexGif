package ru.petrukhin.alexgif.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.rate-service.name}", url = "${feign.rate-service.url}")
public interface ExchangeRatesFeignClient {
    @GetMapping(value = "/api/historical/{date.json}")
    String getRates(@PathVariable("date.json") String date,@RequestParam("app_id") String appId, @RequestParam String base, @RequestParam String symbols);
}
