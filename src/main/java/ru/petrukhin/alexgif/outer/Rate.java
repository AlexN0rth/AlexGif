package ru.petrukhin.alexgif.outer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Accessors(chain = true)
public class Rate {
    private String date;
    private Map<String, Double> rates;
}
