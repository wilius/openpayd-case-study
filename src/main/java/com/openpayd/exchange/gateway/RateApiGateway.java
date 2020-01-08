package com.openpayd.exchange.gateway;

import com.openpayd.exchange.gateway.dto.ExchangeRateResponse;
import com.openpayd.exchange.gateway.dto.ServiceErrorResponse;
import com.openpayd.exchange.gateway.jackson.ObjectMapperFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Currency;

public class RateApiGateway {
    private final HttpClient httpClient;
    private HttpHost host = new HttpHost("api.ratesapi.io", 443, "https");

    public RateApiGateway() {
        httpClient = ExchangeHttpClient.createHttpClient();
    }

    public BigDecimal getExchangeRate(Currency source,
                                      Currency target) {

        HttpGet request;
        try {
            URIBuilder builder = new URIBuilder("https://api.ratesapi.io/api/latest")
                    .setParameter("base", source.getCurrencyCode())
                    .setParameter("symbols", target.getCurrencyCode());
            request = new HttpGet(builder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(host, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
            try {
                ExchangeRateResponse response = ObjectMapperFactory.instance.readValue(httpResponse.getEntity().getContent(), ExchangeRateResponse.class);
                return response.getRates().get(target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        BufferedHttpEntity entity = null;
        ServiceErrorResponse response;
        try {
            entity = new BufferedHttpEntity(httpResponse.getEntity());
            response = ObjectMapperFactory.instance.readValue(entity.getContent(), ServiceErrorResponse.class);
        } catch (IOException e) {
            if (entity != null) {
                final int bufferSize = 1024;
                final char[] buffer = new char[bufferSize];
                final StringBuilder out = new StringBuilder();
                try {
                    Reader in = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
                    int charsRead;
                    while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
                        out.append(buffer, 0, charsRead);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(e);
                }

                throw new RuntimeException(out.toString());
            }

            throw new RuntimeException(e.getMessage());
        }

        throw new RuntimeException(response.getError());
    }
}
