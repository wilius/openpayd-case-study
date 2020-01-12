package com.openpayd.exchange.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.exchange.exception.RemoteException;
import com.openpayd.exchange.gateway.dto.ExchangeRateResponse;
import com.openpayd.exchange.gateway.dto.ServiceErrorResponse;
import com.openpayd.commons.jackson.ObjectMapperFactory;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Currency;

public class RateApiGateway {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = ObjectMapperFactory.instance;

    public RateApiGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getExchangeRate(Currency source,
                                      Currency target) {

        try {
            URIBuilder builder = new URIBuilder("https://api.ratesapi.io/api/latest")
                    .setParameter("base", source.getCurrencyCode())
                    .setParameter("symbols", target.getCurrencyCode());

            ExchangeRateResponse response = restTemplate.getForObject(builder.build(), ExchangeRateResponse.class);
            return response.getRates().get(target);
        } catch (RestClientResponseException e) {
            ServiceErrorResponse response;
            try {
                response = objectMapper.readValue(e.getResponseBodyAsByteArray(), ServiceErrorResponse.class);
            } catch (Throwable t) {
                throw new RemoteException(e);
            }

            throw new RemoteException(response.getError());
        } catch (Throwable t) {
            throw new RemoteException(t);
        }
    }
}
