package me.jarad.rates.service.impl;

import com.google.common.collect.Lists;
import me.jarad.rates.handler.WrongRequestException;
import me.jarad.rates.model.RateEntryEntity;
import me.jarad.rates.model.RateResponse;
import me.jarad.rates.repository.RateEntryRepository;
import me.jarad.rates.service.RateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RateServiceImpl implements RateService {

    private RateEntryRepository repository;
    private ModelMapper modelMapper;

    @Autowired
    public RateServiceImpl(ModelMapper modelMapper, RateEntryRepository repository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public Optional<RateResponse> getCurrentRate() {

        LocalDate nowDate = LocalDate.now();
        Optional<RateEntryEntity> currentRate = repository.findTop1ByDateLessThanEqualOrderByDateDesc(nowDate);

        if (currentRate.isPresent()) {
            RateResponse response = modelMapper.map(currentRate.get(), RateResponse.class);
            return Optional.ofNullable(response);
        } else {
            return Optional.empty();
        }


    }


    public List<RateResponse> getRateHistory(LocalDate from, LocalDate to) throws WrongRequestException {




        List<RateEntryEntity> currentRate = repository.findByDateBetweenOrderByDateAsc(from,to);

        if (!currentRate.isEmpty()) {
            return currentRate.stream()
                    .map(r -> modelMapper.map(r,RateResponse.class))
                    .collect(Collectors.toList());
        } else {
            return Lists.newArrayList();
        }


    }


}
