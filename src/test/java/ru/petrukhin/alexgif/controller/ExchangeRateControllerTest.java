package ru.petrukhin.alexgif.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.petrukhin.alexgif.httpclient.ExchangeRatesFeignClient;
import ru.petrukhin.alexgif.inner.Inner;
import ru.petrukhin.alexgif.inner.InnerData;
import ru.petrukhin.alexgif.outer.Gif;
import ru.petrukhin.alexgif.outer.GifData;
import ru.petrukhin.alexgif.outer.Rate;
import ru.petrukhin.alexgif.service.GifService;
import ru.petrukhin.alexgif.service.InnerService;
import ru.petrukhin.alexgif.service.RateService;

import java.time.LocalDate;
import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExchangeRatesController.class)
class ExchangeRateControllerTest {
    HashMap<String, Double> map = new HashMap<>();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    GifService gifService;
    @MockBean
    RateService rateService;
    @MockBean
    InnerService innerService;
    @MockBean
    ExchangeRatesFeignClient exchangeRatesFeignClient;
    //language=JSON
    private String response = "{\n" +
            "  \"rates\": {\n" +
            "    \"RUB\": 73.123\n" +
            "  }\n" +
            "}";
    private String symbols;
    @Value("${feign.rate-service.app-id}")
    private String appId;
    @Value("${feign.rate-service.base-currency}")
    private String baseCurrency;
    private LocalDate today = LocalDate.now();
    private LocalDate yesterday = LocalDate.now().minusDays(1);
    private Inner inner = new Inner();
    private InnerData innerData = new InnerData();
    private Rate todayRate = new Rate();
    private Rate yesterdayRate = new Rate();
    private Gif gif = new Gif();
    private GifData data = new GifData();

    @BeforeEach
    void setUp() {
        symbols = "RUB";
        innerData.setSymbols(symbols);
        inner.setData(innerData);
        map.put(symbols, 73.123);
        todayRate.setRates(map);
        todayRate.setDate(String.valueOf(today));
        yesterdayRate.setRates(map);
        yesterdayRate.setDate(String.valueOf(yesterday));
        data.setGifUrl("gifUrl");
        gif.setData(data);

    }

    @Test
    void handleRate_OK() throws Exception {
        when(innerService.handleInnerJson()).thenReturn(symbols);
        when(exchangeRatesFeignClient.getRates(today + ".json", appId, baseCurrency, symbols)).thenReturn(response);
        when(exchangeRatesFeignClient.getRates(yesterday + ".json", appId, baseCurrency, symbols)).thenReturn(response);
        when(rateService.handleResponse(response, today)).thenReturn(todayRate);
        when(rateService.handleResponse(response, yesterday)).thenReturn(yesterdayRate);
        when(gifService.handleGif(todayRate, yesterdayRate, symbols)).thenReturn(gif);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/rates"))
                .andExpect(status().isOk());
    }
    @Test
    void handleRate_BadRequest() throws Exception {
        when(innerService.handleInnerJson()).thenReturn(symbols);
        when(exchangeRatesFeignClient.getRates(today + ".json", appId, baseCurrency, symbols)).thenReturn(response);
        when(exchangeRatesFeignClient.getRates(yesterday + ".json", appId, baseCurrency, symbols)).thenReturn(response);
        when(rateService.handleResponse(response, today)).thenReturn(todayRate);
        when(rateService.handleResponse(response, yesterday)).thenReturn(yesterdayRate);
        when(gifService.handleGif(todayRate, yesterdayRate, symbols)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/rates"))
                .andExpect(status().isBadRequest());
    }
}