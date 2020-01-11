package com.openpayd.exchange.mapper;

import com.openpayd.exchange.data.entity.Exchange;
import com.openpayd.exchange.dto.ExchangeDto;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExchangeMapper {
    private ExchangeMapper() {
    }

    public static List<ExchangeDto> map(Exchange... exchanges) {
        return Arrays.stream(exchanges)
                .filter(Objects::nonNull)
                .map(ExchangeMapper::map)
                .collect(Collectors.toList());
    }

    public static List<ExchangeDto> map(List<Exchange> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        return list
                .stream()
                .map(ExchangeMapper::map)
                .collect(Collectors.toList());
    }

    private static ExchangeDto map(Exchange exchange) {
        return new ExchangeDto(
                exchange.getId(),
                exchange.getExchangeDate(),
                exchange.getSourceCurrency(),
                exchange.getTargetCurrency(),
                exchange.getRate(),
                exchange.getCalculated());
    }
}
