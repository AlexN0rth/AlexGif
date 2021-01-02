package ru.petrukhin.alexgif;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RateResponse {
    private Rates rates;
}
