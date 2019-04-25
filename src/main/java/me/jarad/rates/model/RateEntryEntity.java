package me.jarad.rates.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Vitalii Yarmolenko (yarmol@gmail.com) on 24.04.19.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = {"date","currencySource","currencyDestination","rate"})
@Entity(name = "rates")
public class RateEntryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate date;
    private String currencySource;
    private String currencyDestination;
    private BigDecimal rate;

}
