package com.openpayd.exchange.gateway.serdes;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.openpayd.exchange.exception.InvalidCurrencyCodeException;

import java.io.IOException;
import java.util.Currency;
import java.util.Locale;

public class CurrencyDeserializer extends JsonDeserializer<Currency> {

    @Override
    public Currency deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        String code;
        try {
            code = jsonParser.getText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String upperCased = code.toUpperCase(Locale.ENGLISH);
        try {
            return Currency.getInstance(upperCased);
        } catch (Exception t) {
            throw new InvalidCurrencyCodeException(code);
        }
    }
}
