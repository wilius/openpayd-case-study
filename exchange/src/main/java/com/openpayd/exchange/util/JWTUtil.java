package com.openpayd.exchange.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openpayd.commons.jackson.ObjectMapperFactory;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;

public class JWTUtil {
    private JWTUtil() {
    }

    public static String encrypt(Object object) throws JsonProcessingException {
        return Jwts.builder()
                .claim("item", ObjectMapperFactory.instance.writeValueAsString(object))
                .signWith(SignatureAlgorithm.HS256, Constants.JWT.SECRET)
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();
    }

    public static <T> T decrypt(String token, Class<T> clazz) throws IOException {
        String data = Jwts.parser()
                .setSigningKey(Constants.JWT.SECRET)
                .parseClaimsJws(token)
                .getBody()
                .get("item", String.class);

        return ObjectMapperFactory.instance.readValue(data, clazz);
    }
}
