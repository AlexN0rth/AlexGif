package ru.petrukhin.alexgif.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.petrukhin.alexgif.outer.Rate;

import java.time.LocalDate;

@Slf4j
@Service
public class RateService {
    private final ObjectMapper mapper = new ObjectMapper();

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
