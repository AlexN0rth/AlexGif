package ru.petrukhin.alexgif.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This interface represents the Feign Client, which sends a request to the API of the external service "Open Exchange Rates"
 *
 * @author Alex
 * @see ru.petrukhin.alexgif.controller.ExchangeRatesController
 */
@FeignClient(name = "${feign.rate-service.name}", url = "${feign.rate-service.url}")
public interface ExchangeRatesFeignClient {
    @GetMapping(value = "/api/historical/{date.json}")
    String getRates(@PathVariable("date.json") String date, @RequestParam("app_id") String appId, @RequestParam String base, @RequestParam String symbols);
}
