package com.openpayd.exchange.gateway;

import com.openpayd.exchange.exception.RemoteException;
import com.openpayd.exchange.gateway.dto.ExchangeRateResponse;
import com.openpayd.exchange.gateway.dto.ServiceErrorResponse;
import com.openpayd.exchange.gateway.jackson.ObjectMapperFactory;
import com.openpayd.exchange.util.IOUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;

import java.math.BigDecimal;
import java.util.Currency;

public class RateApiGateway {
    private final HttpClient httpClient;

    public RateApiGateway() {
        httpClient = ExchangeHttpClient.createHttpClient();
    }

    public BigDecimal getExchangeRate(Currency source,
                                      Currency target) {

        HttpResponse httpResponse;
        try {
            URIBuilder builder = new URIBuilder("https://api.ratesapi.io/api/latest")
                    .setParameter("base", source.getCurrencyCode())
                    .setParameter("symbols", target.getCurrencyCode());
            HttpGet request = new HttpGet(builder.build());
            httpResponse = httpClient.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                ExchangeRateResponse response = ObjectMapperFactory.instance.readValue(httpResponse.getEntity().getContent(), ExchangeRateResponse.class);
                return response.getRates().get(target);
            }
        } catch (Throwable t) {
            throw new RemoteException(t);
        }

        BufferedHttpEntity entity = null;
        ServiceErrorResponse response;
        try {
            entity = new BufferedHttpEntity(httpResponse.getEntity());
            response = ObjectMapperFactory.instance.readValue(entity.getContent(), ServiceErrorResponse.class);
        } catch (Throwable t) {
            if (entity != null) {
                String content;
                try {
                    content = IOUtil.readStreamContent(entity);
                } catch (Throwable t2) {
                    throw new RemoteException(t2);
                }

                throw new RemoteException(content);
            }

            throw new RemoteException(t.getMessage());
        }

        throw new RemoteException(response.getError());
    }
}
