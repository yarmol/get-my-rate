package me.jarad.rates.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.jarad.rates.model.DateValueItem;
import me.jarad.rates.model.InitialSettingsEntity;
import me.jarad.rates.model.RateEntryEntity;
import me.jarad.rates.repository.InitialSettingsRepository;
import me.jarad.rates.repository.RateEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static me.jarad.rates.utils.ApplicationConstants.CURRENCY_BTC;
import static me.jarad.rates.utils.ApplicationConstants.CURRENCY_USD;


/**
 * Inititial embedded DB loading
 */
@Configuration
@Slf4j
public class ApplicationConfig {

    private InitialSettingsRepository initRepository;
    private RateEntryRepository rateRepository;
    private ObjectMapper mapper;

    @Autowired
    public ApplicationConfig(ObjectMapper mapper,
                             InitialSettingsRepository initRepository,
                             RateEntryRepository rateRepository ) {
        this.initRepository = initRepository;
        this.rateRepository = rateRepository;
        this.mapper = mapper;
    }

    @PostConstruct
    @Transactional
    public void init() {
        List<InitialSettingsEntity> initialSettings = Lists.newArrayList(initRepository.findAll());
        if (initialSettings.isEmpty()) {

            log.info("Start initial loading..");
            try {
                URL resource = this.getClass().getClassLoader().getResource("data/rates.json");

                BufferedReader reader =
                        new BufferedReader(new FileReader(resource.getFile()));
                String initDataLines = getAllLines(reader);

                TypeReference<List<DateValueItem>> dateValueListType = new TypeReference<List<DateValueItem>>() {};
                List<DateValueItem> ratesList = mapper.readValue(initDataLines, dateValueListType);

                for (DateValueItem rateItem : ratesList) {
                    RateEntryEntity rateEntity = RateEntryEntity.builder()
                            .date(rateItem.getDate())
                            .currencySource(CURRENCY_BTC)
                            .currencyDestination(CURRENCY_USD)
                            .rate(rateItem.getValue())
                            .build();
                    rateRepository.save(rateEntity);
                }

                InitialSettingsEntity initItem = InitialSettingsEntity.builder()
                        .date(LocalDate.now())
                        .build();
                initRepository.save(initItem);

            } catch (FileNotFoundException fileNotFoundException) {
                log.error("Initial file not found. Application is not workable, {}", fileNotFoundException.getMessage());
                throw new RuntimeException("Init data file not found", fileNotFoundException);
            } catch (IOException ioException) {
                log.error("Initial file was not loaded. Application is not workable, {}", ioException.getMessage());
                throw new RuntimeException("Init data loading error", ioException);
            }
            log.info("Start initial finished");

        }
    }

    private String getAllLines(BufferedReader reader) {
        return reader.lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }



}
