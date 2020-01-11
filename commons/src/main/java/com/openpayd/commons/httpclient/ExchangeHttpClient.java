package com.openpayd.commons.httpclient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class ExchangeHttpClient {
    private static final int TIMEOUT = 15_000;
    private static final int MAX_CONNECTION_PER_ROUTE = 100;

    public static HttpClient createHttpClient() {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setSocketTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .setContentCompressionEnabled(true)
                .setMaxRedirects(10)
                .setAuthenticationEnabled(false)
                .setExpectContinueEnabled(false)
                .setRedirectsEnabled(false)
                .setRelativeRedirectsAllowed(false);

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(TIMEOUT)
                .setSoKeepAlive(true)
                .setSoReuseAddress(true)
                .setTcpNoDelay(true)
                .build();

        SSLContext sslcontext;
        try {
            sslcontext = SSLContexts.custom()
                    .build();

            sslcontext.init(null, new X509TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{
                    };
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, new DefaultHostnameVerifier());

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager(registry);
        pool.setMaxTotal(MAX_CONNECTION_PER_ROUTE);
        pool.setDefaultMaxPerRoute(MAX_CONNECTION_PER_ROUTE);
        pool.setDefaultSocketConfig(socketConfig);

        return HttpClients.custom()
                .setSSLSocketFactory(factory)
                .setDefaultRequestConfig(requestConfigBuilder.build())
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .disableAutomaticRetries()
                .setSSLContext(sslcontext)
                .setConnectionTimeToLive(2, TimeUnit.MINUTES)
                .setConnectionManager(pool)
                .build();
    }
}
