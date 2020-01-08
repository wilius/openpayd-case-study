package com.openpayd.exchange.config;

import com.openpayd.exchange.gateway.RateApiGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {

    @Bean
    public RateApiGateway rateApiGateway() {
        return new RateApiGateway();
    }
}
