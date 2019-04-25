package me.jarad.rates.service;

import me.jarad.rates.handler.WrongRequestException;
import me.jarad.rates.model.RateResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RateService {

    Optional<RateResponse> getCurrentRate();

    List<RateResponse> getRateHistory(LocalDate from, LocalDate to) throws WrongRequestException;

}
