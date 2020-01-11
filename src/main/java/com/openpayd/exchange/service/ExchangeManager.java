package com.openpayd.exchange.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.openpayd.exchange.data.entity.Exchange;
import com.openpayd.exchange.data.service.ExchangeService;
import com.openpayd.exchange.exception.ExchangeException;
import com.openpayd.exchange.gateway.RateApiGateway;
import com.openpayd.exchange.model.ExchangeListPageToken;
import com.openpayd.exchange.util.MathUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import static com.openpayd.exchange.util.CacheLoaderUtil.create;

@Service
public class ExchangeManager {

    private final LoadingCache<Pair<Currency, Currency>, BigDecimal> rateCache;
    private final ExchangeService conversionService;

    public ExchangeManager(RateApiGateway gateway,
                           ExchangeService conversionService) {
        this.rateCache = createRateCache(gateway);
        this.conversionService = conversionService;
    }

    public BigDecimal getRate(Currency source,
                              Currency target) {
        ConcurrentMap<Pair<Currency, Currency>, BigDecimal> map = rateCache.asMap();

        Pair<Currency, Currency> pair = Pair.of(source, target);
        Pair<Currency, Currency> reverse = Pair.of(target, source);

        BigDecimal rate = map.get(pair);
        if (rate != null) {
            return rate;
        }

        rate = map.get(reverse);
        if (rate != null) {
            return MathUtil.divide(BigDecimal.ONE, rate);
        }

        Throwable t = null;
        for (int i = 0; i < 10; i++) {
            try {
                return rateCache.get(pair);
            } catch (Throwable e) {
                t = e;
            }
        }

        if (t.getCause() instanceof ExchangeException) {
            throw (ExchangeException) t.getCause();
        }

        throw new RuntimeException(t);
    }

    public Exchange exchange(Currency source, Currency target, BigDecimal amount) {
        BigDecimal rate = getRate(source, target);
        return conversionService.create(source, target, rate, amount.multiply(rate));
    }

    private LoadingCache<Pair<Currency, Currency>, BigDecimal> createRateCache(RateApiGateway gateway) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(create(x -> {
                    BigDecimal rate = gateway.getExchangeRate(x.getFirst(), x.getSecond());
                    return MathUtil.scale(rate);
                }));
    }

    public Exchange get(Long id, LocalDate date) {
        Exchange exchange = conversionService.findById(id);
        if (exchange != null && !exchange.getExchangeDate().toLocalDate().equals(date)) {
            return null;
        }

        return exchange;
    }

    public Page<Exchange> list(LocalDate date, int size) {
        return conversionService.list(date, size);
    }

    public Page<Exchange> list(ExchangeListPageToken page) {
        return conversionService.list(page.getDate(), page.getPageNumber(), page.getPageSize(), page.getMaxId());
    }
}
