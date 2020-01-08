package com.openpayd.exchange.data.service;

import com.openpayd.exchange.data.repository.ConversionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @Service
public class ConversionService {
    private final Logger log = LoggerFactory.getLogger(ConversionService.class);

    private final ConversionRepository repository;

    public ConversionService(ConversionRepository repository) {
        this.repository = repository;
    }
}
