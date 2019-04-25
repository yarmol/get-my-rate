package me.jarad.rates.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.jarad.rates.RatesApplication;
import me.jarad.rates.handler.WrongRequestException;
import me.jarad.rates.model.RateEntryEntity;
import me.jarad.rates.model.RateResponse;
import me.jarad.rates.repository.RateEntryRepository;
import me.jarad.rates.service.RateService;
import me.jarad.rates.service.impl.RateServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.jarad.rates.utils.ApplicationConstants.CURRENCY_BTC;
import static me.jarad.rates.utils.ApplicationConstants.CURRENCY_USD;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@Slf4j
@SpringBootTest(classes = {RatesApplication.class})
public class RateControllerMvcTest {


    private MockMvc mvc;

    @MockBean
    private RateService rateService;

    private static ObjectMapper objectMapper;

    @MockBean
    private RateEntryRepository mockedRateRepository;


    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Before
    public void setUpControllerMvcTest() throws Exception, WrongRequestException {

        mvc    = webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        ModelMapper modelMapper = new ModelMapper();

        LocalDate usedDate = LocalDate.of(2019,04,25);
        BigDecimal actualValue = BigDecimal.valueOf(5388.22);

        RateEntryEntity rateEntry = RateEntryEntity.builder()
                .date(usedDate)
                .rate(actualValue)
                .currencySource(CURRENCY_BTC)
                .currencyDestination(CURRENCY_USD)
                .build();

        Mockito.when(mockedRateRepository
                .findTop1ByDateLessThanEqualOrderByDateDesc(usedDate))
                .thenReturn(Optional.of(rateEntry));

        Mockito.when(rateService
                .getCurrentRate())
                .thenReturn(Optional.of(modelMapper.map(rateEntry,RateResponse.class)));


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void whenCallGetCurrentRate_thenCorrect() throws Exception {


        ResultActions resultActions = mvc.perform(
                get("/api/current").characterEncoding("UTF-8"));

        assertEquals(200,resultActions.andReturn().getResponse().getStatus());

        JsonNode resultNode = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), JsonNode.class);


        assertEquals(5388.22,resultNode.get("rate").asDouble(), 0.001);


    }

    @Test
    public void whenCallGetHistory_thenCorrect() throws Exception {
        LocalDate dateFrom = LocalDate.of(2018,01,29);
        LocalDate dateTo =  LocalDate.of(2018,01,30);

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


        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("from", dateFrom.format(DateTimeFormatter.ISO_LOCAL_DATE));
        params.add("to", dateFrom.format(DateTimeFormatter.ISO_LOCAL_DATE));

        ResultActions resultActions = mvc.perform(
                get("/api/history")
                        .characterEncoding("UTF-8")
                        .params(params));

        assertEquals(200,resultActions.andReturn().getResponse().getStatus());

        JsonNode arrayNode = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), JsonNode.class);

        for (JsonNode jsonNode : arrayNode) {
            RateResponse responseItem = objectMapper.treeToValue(jsonNode, RateResponse.class);

            if (responseItem.getDate().equals(dateFrom)) {
                assertEquals(responseItem.getRate(),BigDecimal.valueOf(11446.54));
            } else {
                assertEquals(responseItem.getRate(),BigDecimal.valueOf(11685.58));
            }
        }

    }


    @Test
    public void whenCallGetHistory_thenError() throws Exception {
        LocalDate dateFrom = LocalDate.of(2018,01,30);
        LocalDate dateTo =   LocalDate.of(2018,01,29);


        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("from", dateFrom.format(DateTimeFormatter.ISO_LOCAL_DATE));
        params.add("to", dateTo.format(DateTimeFormatter.ISO_LOCAL_DATE));

        ResultActions result = mvc.perform(
                get("/api/history")
                        .characterEncoding("UTF-8")
                        .params(params));


        assertEquals(400,result.andReturn().getResponse().getStatus());

    }
}