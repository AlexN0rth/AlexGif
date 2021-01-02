package ru.petrukhin.alexgif;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Accessors(chain = true)
public class Rates {
    @JsonProperty("BYN")
    private double byn;
    @JsonProperty("RUB")
    private double rub;
}
