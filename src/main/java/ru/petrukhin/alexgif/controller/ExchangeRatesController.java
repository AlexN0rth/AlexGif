package ru.petrukhin.alexgif.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.petrukhin.alexgif.httpclient.ExchangeRatesFeignClient;
import ru.petrukhin.alexgif.outer.Gif;
import ru.petrukhin.alexgif.outer.Rate;
import ru.petrukhin.alexgif.service.GifService;
import ru.petrukhin.alexgif.service.InnerService;
import ru.petrukhin.alexgif.service.RateService;

import java.time.LocalDate;

/**
 * This class represents a REST Controller for receiving requests from the client.
 * It contains a {@code handleRate()} method that processes currency exchange rate information received from the external currency service
 * and returns a ResponseEntity object which contains a gif object and http status.
 *
 * @author Alex
 * @see ru.petrukhin.alexgif.service.InnerService
 * @see ru.petrukhin.alexgif.service.RateService
 * @see ru.petrukhin.alexgif.service.GifService
 * @see ru.petrukhin.alexgif.httpclient.ExchangeRatesFeignClient
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rates")
public class ExchangeRatesController {
    private final GifService gifService;
    private final RateService rateService;
    private final InnerService innerService;
    @Autowired
    ExchangeRatesFeignClient exchangeRatesFeignClient;
    private LocalDate today = LocalDate.now();
    private LocalDate yesterday = today.minusDays(1);

    @Value("${feign.rate-service.app-id}")
    private String appId;
    @Value("${feign.rate-service.base-currency}")
    private String baseCurrency;

    @GetMapping
    public @ResponseBody
    ResponseEntity<Gif> handleRate() {
        String symbols = innerService.handleInnerJson();
        if (!(symbols.equals("RUB") || symbols.equals("BYN"))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Rate todayRate;
        Rate yesterdayRate;
        try {
            todayRate = rateService.handleResponse(exchangeRatesFeignClient.getRates(today + ".json", appId, baseCurrency, symbols), today);
            yesterdayRate = rateService.handleResponse(exchangeRatesFeignClient.getRates(yesterday + ".json", appId, baseCurrency, symbols), yesterday);
        } catch (FeignException e) {
            log.info("Rate service is not responding");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Gif gif = gifService.handleGif(todayRate, yesterdayRate, symbols);
        if (gif == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gif, HttpStatus.OK);

    }


}
