package ru.petrukhin.alexgif.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.petrukhin.alexgif.httpclient.GiphyFeignClient;
import ru.petrukhin.alexgif.outer.Gif;
import ru.petrukhin.alexgif.outer.Rate;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This class represents an internal service for a REST Controller (ExchangeRatesController).
 * It accesses an external service through the Feign Client (giphyFeignClient).
 *
 * @author Alex
 * @see GiphyFeignClient
 * @see ru.petrukhin.alexgif.controller.ExchangeRatesController
 * @see ru.petrukhin.alexgif.service.RateService
 */
@Slf4j
@Service
public class GifService {
    private static String tag = "broke";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    GiphyFeignClient giphyFeignClient;
    @Value("${feign.gif-service.app-id}")
    private String appId;
    @Value("${json.outer.path}")
    private String path;

    /**
     * This method compares the exchange rate values of two Rate objects.
     * Based on the comparison result, passes the required tag to send the request through the Feign Client
     *
     * @param todayRate     Rate object received from the internal service named RateService that contains the exchange rate for today
     * @param yesterdayRate Rate object received from the internal service named RateService that contains the exchange rate for yesterday
     * @param symbols       A string value obtained from an internal service named InnerService that contains 3-letter currency code
     * @return Gif object containing the Gif URL received from the external Giphy service
     */
    public Gif handleGif(Rate todayRate, Rate yesterdayRate, String symbols) {
        Gif gif = null;
        if (todayRate == null || yesterdayRate == null) {
            return gif;
        }
        Double todayValue = todayRate.getRates().get(symbols);
        Double yesterdayValue = yesterdayRate.getRates().get(symbols);
        if (todayValue >= yesterdayValue) {
            tag = "rich";
        }
        try {
            gif = mapper.readValue(giphyFeignClient.getGif(appId, tag), Gif.class);
        } catch (FeignException e) {
            log.info("Gif service is not responding");
            return gif;
        } catch (JsonProcessingException e) {
            log.info("Read gif response exception");
        }
        try {
            createJson(gif);
        } catch (IOException e) {
            log.info("JSON file write exception");
        }
        return gif;
    }

    /**
     * This method uses the ObjectMapper from the Jackson library to write the value from the Gif object to a JSON file (outer.json).
     *
     * @param gif Gif object containing the Gif URL received from the external Giphy service
     * @throws IOException e
     */
    private void createJson(Gif gif) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            mapper.writeValue(writer, gif);
        }
    }
}
