package me.jarad.rates.controller;

import me.jarad.rates.model.RateEntryEntity;
import me.jarad.rates.model.RateResponse;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static me.jarad.rates.utils.ApplicationConstants.CURRENCY_BTC;
import static me.jarad.rates.utils.ApplicationConstants.CURRENCY_USD;
import static org.junit.Assert.assertEquals;

public class MapperTest {

    private static ModelMapper modelMapper;

    @Before
    public void setUpMapperTest() throws Exception {
        modelMapper = new ModelMapper();
    }

    @Test
    public void whenEntityToDto_thenCorrect() {
        LocalDate usedDate = LocalDate.of(2018,01,29);

        RateEntryEntity entry1 = RateEntryEntity.builder()
                .date(usedDate)
                .rate(BigDecimal.valueOf(11446.54))
                .currencySource(CURRENCY_BTC)
                .currencyDestination(CURRENCY_USD)
                .build();


        RateResponse rateResponse = modelMapper.map(entry1, RateResponse.class);


        assertEquals(entry1.getDate(),rateResponse.getDate());
        assertEquals(entry1.getCurrencyDestination(),rateResponse.getCurrencyDestination());
        assertEquals(entry1.getCurrencySource(),rateResponse.getCurrencySource());
        assertEquals(entry1.getRate(),rateResponse.getRate());

    }

    @Test
    public void whenEntityFromDto_thenCorrect() {

        LocalDate usedDate = LocalDate.of(2018,01,29);
        RateResponse rateResponse = RateResponse.builder()
                .date(usedDate)
                .rate(BigDecimal.valueOf(11446.54))
                .currencySource(CURRENCY_BTC)
                .currencyDestination(CURRENCY_USD)
                .build();
        RateEntryEntity entry1 = modelMapper.map(rateResponse, RateEntryEntity.class);

        assertEquals(entry1.getDate(),rateResponse.getDate());
        assertEquals(entry1.getCurrencyDestination(),rateResponse.getCurrencyDestination());
        assertEquals(entry1.getCurrencySource(),rateResponse.getCurrencySource());
        assertEquals(entry1.getRate(),rateResponse.getRate());


    }
}
