package com.openpayd.exchange.gateway.jackson.serdes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.openpayd.exchange.model.PageToken;
import com.openpayd.exchange.util.JWTUtil;

import java.io.IOException;
import java.net.URLEncoder;

public class PageTokenSerializer extends JsonSerializer<PageToken> {
    @Override
    public void serialize(PageToken pageToken, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(URLEncoder.encode(JWTUtil.encrypt(pageToken), "UTF8"));
    }
}
