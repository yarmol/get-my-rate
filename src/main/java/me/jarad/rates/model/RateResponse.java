package me.jarad.rates.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("Rate response value")
public class RateResponse {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty("Date as yyyy-mm-dd")
    private LocalDate date;

    @ApiModelProperty("Code of source current")
    @JsonProperty("currency_from")
    private String currencySource;

    @ApiModelProperty("Code of destination current")
    @JsonProperty("currency_to")
    private String currencyDestination;

    @ApiModelProperty("Rate value")
    private BigDecimal rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateResponse that = (RateResponse) o;
        return Objects.equal(date, that.date) &&
                Objects.equal(currencySource, that.currencySource) &&
                Objects.equal(currencyDestination, that.currencyDestination) &&
                Objects.equal(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(date, currencySource, currencyDestination, rate);
    }
}
