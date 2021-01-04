package ru.petrukhin.alexgif.controller;

import lombok.RequiredArgsConstructor;
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
import ru.petrukhin.alexgif.service.GifService;
import ru.petrukhin.alexgif.service.InnerService;
import ru.petrukhin.alexgif.service.RateService;

import java.io.IOException;
import java.time.LocalDate;

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
    ResponseEntity<Gif> handleRate() throws IOException {
        String symbols = innerService.handleInnerJson();
        if (!(symbols.equals("RUB") || symbols.equals("BYN"))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String todayResponse = exchangeRatesFeignClient.getRates(today + ".json", appId, baseCurrency, symbols);
        String yesterdayResponse = exchangeRatesFeignClient.getRates(yesterday + ".json", appId, baseCurrency, symbols);
        if (todayResponse == null || todayResponse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (yesterdayResponse == null || yesterdayResponse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Gif gif = gifService.handleGif(rateService.handleResponse(todayResponse, today), rateService.handleResponse(yesterdayResponse, yesterday), symbols);
        if (gif == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(gif, HttpStatus.OK);

    }


}
