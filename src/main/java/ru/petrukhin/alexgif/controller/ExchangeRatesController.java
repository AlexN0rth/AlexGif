package ru.petrukhin.alexgif.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.petrukhin.alexgif.httpclient.ExchangeRatesFeignClient;
import ru.petrukhin.alexgif.inner.Inner;
import ru.petrukhin.alexgif.outer.Gif;
import ru.petrukhin.alexgif.outer.Rates;
import ru.petrukhin.alexgif.service.GifService;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rates")
public class ExchangeRatesController {
    private final ObjectMapper mapper = new ObjectMapper();
    private final GifService gifService;
    @Autowired
    ExchangeRatesFeignClient exchangeRatesFeignClient;
    private LocalDate today = LocalDate.now();
    private LocalDate yesterday = today.minusDays(1);

    @Value("${feign.rate-service.app-id}")
    private String appId;
    @Value("${feign.rate-service.base-currency}")
    private String baseCurrency;
    @Value("${json.inner.path}")
    private String path;

    @GetMapping
    public @ResponseBody
    Gif handleRate() throws IOException {
        try (FileReader reader = new FileReader(path)) {
            Inner inner = mapper.readValue(reader, Inner.class);
            Rates todayRates = handleQuery(exchangeRatesFeignClient.getRates(today + ".json", appId, baseCurrency, inner.getData().getSymbols()), today);
            Rates yesterdayRates = handleQuery(exchangeRatesFeignClient.getRates(yesterday + ".json", appId, baseCurrency, inner.getData().getSymbols()), yesterday);

            return gifService.handleGif(todayRates, yesterdayRates, inner);
        }
    }

    private Rates handleQuery(String query, LocalDate date) throws JsonProcessingException {
        Rates rates = mapper.readValue(query, Rates.class);
        rates.setDate(String.valueOf(date));
        return rates;
    }
}
