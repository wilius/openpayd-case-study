package com.openpayd.exchange.data.service;

import com.openpayd.exchange.data.entity.Exchange;
import com.openpayd.exchange.data.repository.ExchangeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
        conversion.setExchangeDate(ZonedDateTime.now());
        conversion.setSourceCurrency(source);
        conversion.setTargetCurrency(target);
        conversion.setRate(rate);
        conversion.setCalculated(calculated);
        return repository.save(conversion);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Exchange findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<Exchange> list(LocalDate date, int page, int size, long maxId) {
        ZonedDateTime start = date.atStartOfDay(ZoneId.systemDefault()).withNano(0);
        ZonedDateTime end = start.plusDays(1).minusNanos(1);
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByExchangeDateBetweenAndIdLessThanEqualOrderByIdDesc(start, end, maxId, pageable);
    }

    public Page<Exchange> list(LocalDate date, int size) {
        ZonedDateTime start = date.atStartOfDay(ZoneId.systemDefault()).withNano(0);
        ZonedDateTime end = start.plusDays(1).minusNanos(1);
        Pageable pageable = PageRequest.of(0, size);
        return repository.findAllByExchangeDateBetweenOrderByIdDesc(start, end, pageable);
    }
}
