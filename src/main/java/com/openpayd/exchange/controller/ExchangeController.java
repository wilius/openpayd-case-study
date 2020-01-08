package com.openpayd.exchange.controller;

import com.openpayd.exchange.dto.response.GetRateResponse;
import com.openpayd.exchange.exception.InvalidCurrencyCodeException;
import com.openpayd.exchange.service.ExchangeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;
import java.util.Locale;

@RestController
public class ExchangeController {
    private final ExchangeService service;

    public ExchangeController(ExchangeService service) {
        this.service = service;
    }

    @GetMapping(path = "/{source}/{target}")
    public GetRateResponse getExchangeRate(@PathVariable("source") String source,
                                           @PathVariable("target") String target) {
        return new GetRateResponse(
                service.getRate(getCurrency(source), getCurrency(target))
        );
    }

    private static Currency getCurrency(String code) {
        String upperCased = code.toUpperCase(Locale.ENGLISH);
        try {
            return Currency.getInstance(upperCased);
        } catch (Exception t) {
            throw new InvalidCurrencyCodeException(code);
        }
    }
}
