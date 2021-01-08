package ru.petrukhin.alexgif.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.petrukhin.alexgif.outer.Rate;

import java.time.LocalDate;

/**
 * This class represents an internal service for a REST Controller (ExchangeRatesController).
 *
 * @author Alex
 * @see ru.petrukhin.alexgif.controller.ExchangeRatesController
 */
@Slf4j
@Service
public class RateService {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * This method uses the ObjectMapper from the Jackson library to read the response and fill the Rate object
     *
     * @param response Response received from an external exchange rate service
     * @param date     Date of the current exchange rate value
     * @return Rate object containing the value of the currency exchange rate
     */
    public Rate handleResponse(String response, LocalDate date) {
        Rate rate = null;
        try {
            rate = mapper.readValue(response, Rate.class);
            rate.setDate(String.valueOf(date));
        } catch (JsonProcessingException e) {
            log.info("Read rate response exception");
            return rate;
        }
        return rate;
    }
}
