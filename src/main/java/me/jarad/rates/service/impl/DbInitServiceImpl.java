package me.jarad.rates.service.impl;

import me.jarad.rates.repository.RateEntryRepository;
import me.jarad.rates.service.DbInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DbInitServiceImpl implements DbInitService {

    private RateEntryRepository repository;

    @Autowired
    public DbInitServiceImpl(RateEntryRepository repository) {
        this.repository =  repository;
    }

    @Override
    public void initialFilling() {

    }
}
