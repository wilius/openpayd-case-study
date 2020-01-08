package com.openpayd.exchange.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.openpayd.exchange.exception.ExchangeException;
import com.openpayd.exchange.gateway.RateApiGateway;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class ExchangeService {

    private final LoadingCache<Pair<Currency, Currency>, BigDecimal> rateCache;

    public ExchangeService(RateApiGateway gateway) {
        this.rateCache = createRateCache(gateway);
    }

    public BigDecimal getRate(Currency source,
                              Currency target) {
        Pair<Currency, Currency> pair = Pair.of(source, target);
        Throwable t = null;
        for (int i = 0; i < 10; i++) {
            try {
                return rateCache.get(pair);
            } catch (Throwable e) {
                t = e;
            }
        }

        if (t.getCause() instanceof ExecutionException) {
            throw (ExchangeException) t.getCause();
        }

        throw new RuntimeException(t.getCause());
    }

    private LoadingCache<Pair<Currency, Currency>, BigDecimal> createRateCache(RateApiGateway gateway) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .build(new CacheLoader<Pair<Currency, Currency>, BigDecimal>() {
                    @Override
                    public BigDecimal load(Pair<Currency, Currency> pair) {
                        return gateway.getExchangeRate(pair.getFirst(), pair.getSecond());
                    }
                });
    }

}
