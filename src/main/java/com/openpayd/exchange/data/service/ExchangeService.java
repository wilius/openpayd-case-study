package com.openpayd.exchange.data.service;

import com.openpayd.exchange.data.entity.Exchange;
import com.openpayd.exchange.data.repository.ExchangeRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Currency;

@Service
public class ExchangeService {
    private final ExchangeRepository repository;

    public ExchangeService(ExchangeRepository repository) {
        this.repository = repository;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Exchange create(Currency source, Currency target, BigDecimal rate, BigDecimal calculated) {
        Exchange conversion = new Exchange();
        conversion.setConversionDate(ZonedDateTime.now());
        conversion.setSourceCurrency(source);
        conversion.setTargetCurrency(target);
        conversion.setRate(rate);
        conversion.setCalculated(calculated);
        return repository.save(conversion);
    }
}
