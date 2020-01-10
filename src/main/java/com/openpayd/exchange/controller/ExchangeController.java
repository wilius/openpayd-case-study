package com.openpayd.exchange.controller;

import com.openpayd.exchange.data.entity.Exchange;
import com.openpayd.exchange.dto.ExchangeDto;
import com.openpayd.exchange.dto.request.ExchangeRequest;
import com.openpayd.exchange.dto.request.GetRateRequest;
import com.openpayd.exchange.dto.request.ListTransactionsRequest;
import com.openpayd.exchange.dto.response.ExchangeResponse;
import com.openpayd.exchange.dto.response.GetRateResponse;
import com.openpayd.exchange.dto.response.PagedResponse;
import com.openpayd.exchange.mapper.ExchangeMapper;
import com.openpayd.exchange.model.ExchangeListPageToken;
import com.openpayd.exchange.service.ExchangeManager;
import com.openpayd.exchange.util.JWTUtil;
import com.openpayd.exchange.util.MathUtil;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

    @RequestMapping(path = "/list/{pageToken}", method = RequestMethod.GET)
    public PagedResponse<ExchangeDto> list(@PathVariable("pageToken") String pageToken,
                                           @RequestParam(name = "page", required = false) Integer page) {
        if (page != null) {
            MathUtil.validatePositive(page, "page should be equals or greater than 1");
        }

        ExchangeListPageToken token;
        try {
            token = JWTUtil.decrypt(pageToken, ExchangeListPageToken.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (page != null) {
            token = new ExchangeListPageToken(token.getMaxId(), token.getDate(), page - 1, token.getPageSize());
        }

        return mapPagedResponse(service.list(token), token.getDate(), token.getPageSize(), token);
    }

    @RequestMapping(path = "/list", method = RequestMethod.POST)
    public PagedResponse<ExchangeDto> list(@RequestBody @Validated ListTransactionsRequest request,
                                           @RequestParam(name = "size", defaultValue = "10") int pageSize) {

        MathUtil.validatePositive(pageSize, "page size should be equals or greater than 1");

        if (request.getId() != null) {
            Exchange item = service.get(request.getId(), request.getDate());
            List<ExchangeDto> result = ExchangeMapper.map(item);

            return new PagedResponse<>(
                    1,
                    result.size(),
                    0,
                    result,
                    null
            );
        }

        Page<Exchange> page = service.list(request.getDate(), pageSize);
        return mapPagedResponse(page, request.getDate(), pageSize, null);
    }

    private PagedResponse<ExchangeDto> mapPagedResponse(Page<Exchange> page,
                                                        LocalDate date,
                                                        int pageSize,
                                                        ExchangeListPageToken previousToken) {

        List<ExchangeDto> elements = ExchangeMapper.map(page.getContent());

        ExchangeListPageToken nextPageToken = null;

        int pageNumber = page.getNumber() + 1;
        if (!elements.isEmpty() && page.hasNext()) {
            long maxId = previousToken != null ? previousToken.getMaxId() : elements.get(0).getId();
            nextPageToken = new ExchangeListPageToken(maxId, date, pageNumber, pageSize);
        }

        return new PagedResponse<>(
                page.getTotalPages(),
                page.getTotalElements(),
                pageNumber,
                elements,
                nextPageToken
        );
    }
}
