package me.jarad.rates.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.jarad.rates.handler.WrongRequestException;
import me.jarad.rates.model.RateEntryEntity;
import me.jarad.rates.model.RateResponse;
import me.jarad.rates.repository.RateEntryRepository;
import me.jarad.rates.service.RateService;
import me.jarad.rates.service.impl.RateServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.jarad.rates.utils.ApplicationConstants.CURRENCY_BTC;
import static me.jarad.rates.utils.ApplicationConstants.CURRENCY_USD;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@Slf4j
public class RateControllerTest {

    private static RateController rateController;
    private static RateService rateService;
    private static RateEntryRepository mockedRateRepository;

    private static LocalDate usedDate;
    private static BigDecimal actualValue;
    private static LocalDate usedDateFrom;
    private static LocalDate usedDateTo;
    private static ObjectMapper objectMapper;

    @Before
    public void setUpControllerInstance() throws Throwable {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        usedDate = LocalDate.of(2019, 04, 25);
        actualValue = BigDecimal.valueOf(5388.22);


        LocalDate usedDateFrom = LocalDate.of(2018, 01, 29);
        LocalDate usedDateTo = LocalDate.of(2018, 01, 30);

        ModelMapper modelMapper = new ModelMapper();
        mockedRateRepository = mock(RateEntryRepository.class);
        rateService = new RateServiceImpl(modelMapper, mockedRateRepository);
        rateController = new RateController(rateService);

        RateEntryEntity rateEntry = RateEntryEntity.builder()
                .date(usedDate)
                .rate(actualValue)
                .currencySource(CURRENCY_BTC)
                .currencyDestination(CURRENCY_USD)
                .build();

        Mockito.when(mockedRateRepository
                .findTop1ByDateLessThanEqualOrderByDateDesc(usedDate))
                .thenReturn(Optional.of(rateEntry));

        List<RateEntryEntity> ratesList = new ArrayList<>();

        RateEntryEntity entry1 = RateEntryEntity.builder()
                .date(usedDateFrom)
                .rate(BigDecimal.valueOf(11446.54))
                .currencySource(CURRENCY_BTC)
                .currencyDestination(CURRENCY_USD)
                .build();

        RateEntryEntity entry2 = RateEntryEntity.builder()
                .date(usedDateTo)
                .rate(BigDecimal.valueOf(11685.58))
                .currencySource(CURRENCY_BTC)
                .currencyDestination(CURRENCY_USD)
                .build();


        ratesList.add(entry1);
        ratesList.add(entry2);

        Mockito.when(mockedRateRepository.findByDateBetweenOrderByDateAsc(usedDateFrom, usedDateTo))
                .thenReturn(ratesList);

        Mockito.when(rateService.getRateHistory(
                LocalDate.of(2018, 12, 29),
                LocalDate.of(2018, 01, 30)))
                .thenAnswer(invocation -> {
                    throw new WrongRequestException("Wrong dates period");}
                );

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void whenCallGetCurrentRate_thenCorrect()  throws Throwable  {
        ResponseEntity<RateResponse> current = rateController.getCurrent();
        assertEquals(current.getStatusCode().value(), 200);
        assertEquals(current.getBody().getRate(), BigDecimal.valueOf(5388.22));

    }

    @Test
    public void whenCallGetHistory_thenCorrect() throws Throwable {
        LocalDate dateFrom = LocalDate.of(2018, 01, 29);
        LocalDate dateTo = LocalDate.of(2018, 01, 30);

        RateResponse entry1 = RateResponse.builder()
                .date(dateFrom)
                .rate(BigDecimal.valueOf(11446.54))
                .currencySource(CURRENCY_BTC)
                .currencyDestination(CURRENCY_USD)
                .build();

        RateResponse entry2 = RateResponse.builder()
                .date(dateTo)
                .rate(BigDecimal.valueOf(11685.58))
                .currencySource(CURRENCY_BTC)
                .currencyDestination(CURRENCY_USD)
                .build();


        ResponseEntity<List<RateResponse>> historyResponse = rateController.getHistory(dateFrom, dateTo);
        List<RateResponse> ratesList = historyResponse.getBody();

        assertEquals(historyResponse.getStatusCode().value(), 200);

        assertEquals(ratesList.get(0), entry1);

        assertEquals(ratesList.get(1), entry2);
    }




}