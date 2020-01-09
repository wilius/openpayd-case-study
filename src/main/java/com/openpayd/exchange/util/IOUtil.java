package com.openpayd.exchange.util;

import org.apache.http.entity.BufferedHttpEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class IOUtil {
    private IOUtil() {
    }

    public static String readStreamContent(BufferedHttpEntity entity) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
        int charsRead;
        while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        return out.toString();
    }
}
