package com.openpayd.exchange.controller;

import com.openpayd.exchange.data.entity.Exchange;
import com.openpayd.exchange.dto.request.ExchangeRequest;
import com.openpayd.exchange.dto.request.GetRateRequest;
import com.openpayd.exchange.dto.response.ExchangeResponse;
import com.openpayd.exchange.dto.response.GetRateResponse;
import com.openpayd.exchange.service.ExchangeManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController {
    private final ExchangeManager service;

    public ExchangeController(ExchangeManager service) {
        this.service = service;
    }

    @RequestMapping(path = "/rate", method = RequestMethod.POST)
    public GetRateResponse getExchangeRate(@RequestBody @Validated GetRateRequest request) {
        return new GetRateResponse(
                service.getRate(request.getSource(), request.getTarget())
        );
    }

    @RequestMapping(path = "/exchange", method = RequestMethod.POST)
    public ExchangeResponse exchange(@RequestBody @Validated ExchangeRequest request) {
        Exchange result = service.exchange(request.getSource(), request.getTarget(), request.getAmount());
        return new ExchangeResponse(
                result.getId(),
                result.getCalculated()
        );
    }
}
