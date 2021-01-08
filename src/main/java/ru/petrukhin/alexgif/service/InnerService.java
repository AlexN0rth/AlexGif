package ru.petrukhin.alexgif.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.petrukhin.alexgif.inner.Inner;

import java.io.FileReader;
import java.io.IOException;

/**
 * * This class represents an internal service for a REST Controller (ExchangeRatesController).
 *
 * @author Alex
 * @see ru.petrukhin.alexgif.controller.ExchangeRatesController
 */
@Slf4j
@Service
public class InnerService {
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${json.inner.path}")
    private String path;

    /**
     * This method uses the ObjectMapper from the Jackson library to read read the incoming JSON file (inner.json) and fill the Inner object
     *
     * @return String value of the currency name obtained from the Inner object
     */
    public String handleInnerJson() {
        Inner inner;
        try (FileReader reader = new FileReader(path)) {
            inner = mapper.readValue(reader, Inner.class);

        } catch (IOException e) {
            log.info("JSON file read exception");
            return "";
        }
        return inner.getData().getSymbols();
    }

}
