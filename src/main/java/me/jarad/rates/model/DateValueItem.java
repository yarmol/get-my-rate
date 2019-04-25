package me.jarad.rates.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.jarad.rates.config.CustomSerializers;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateValueItem implements Serializable {


    @DateTimeFormat(iso = DATE)
    private LocalDate date;

    @JsonSerialize(using = CustomSerializers.BigDecimalToStringSerializer.class)
    @JsonDeserialize(using = CustomSerializers.BigDecimalFromStringDeserializer.class)
    private BigDecimal value;
}
