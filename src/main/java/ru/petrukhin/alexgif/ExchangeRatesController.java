package ru.petrukhin.alexgif;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rates")
public class ExchangeRatesController {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    ExchangeRatesFeignClient exchangeRatesFeignClient;

    @GetMapping
    public @ResponseBody
    String rate() throws JsonProcessingException {

        String rates = exchangeRatesFeignClient.getRates("c5331769149f4f59bd5afce1b0076d45", "USD", "RUB,BYN");
        RateObject rateObject = mapper.readValue(rates, RateObject.class);

        return mapper.writeValueAsString(rateObject);
    }
}
