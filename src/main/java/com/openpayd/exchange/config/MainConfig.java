package com.openpayd.exchange.config;

import com.openpayd.exchange.gateway.ExchangeHttpClient;
import com.openpayd.exchange.gateway.RateApiGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class MainConfig {
    private static final int TIMEOUT = 15_000;
    private static final int MAX_CONNECTION_PER_ROUTE = 100;

    @Bean
    public RateApiGateway rateApiGateway(RestTemplate restTemplate) {
        return new RateApiGateway(restTemplate);
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(ExchangeHttpClient.createHttpClient());
        return new RestTemplate(requestFactory);
    }
}
