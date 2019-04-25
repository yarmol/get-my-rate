package me.jarad.rates.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RateResponse {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @JsonProperty("currency_from")
    private String currencySource;

    @JsonProperty("currency_to")
    private String currencyDestination;

    private BigDecimal rate;

}
