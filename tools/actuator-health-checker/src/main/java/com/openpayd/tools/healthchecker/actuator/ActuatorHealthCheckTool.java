package com.openpayd.tools.healthchecker.actuator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openpayd.commons.httpclient.ExchangeHttpClient;
import com.openpayd.commons.jackson.ObjectMapperFactory;
import com.openpayd.tools.healthchecker.actuator.dto.Health;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ActuatorHealthCheckTool {
    public static void main(String[] args) {
        try {
            Options options = new Options();
            Option urlToTest = new Option("u", "url", true, "http request path");
            urlToTest.setRequired(true);
            options.addOption(urlToTest);

            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();

            CommandLine cmd;
            try {
                cmd = parser.parse(options, args);
            } catch (ParseException e) {
                System.err.println(e.getMessage());
                formatter.printHelp("Docker Heath Check Tool", options);
                System.exit(1);
                return;
            }

            String url = cmd.getOptionValue(urlToTest.getLongOpt());

            HttpClient client = ExchangeHttpClient.createHttpClient();

            HttpResponse httpResponse = client.execute(new HttpGet(url));
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                ObjectMapper mapper = ObjectMapperFactory.instance;
                Health response = mapper.readValue(httpResponse.getEntity().getContent(), Health.class);
                if (Health.Status.UP.equals(response.getStatus())) {
                    System.exit(0);
                }

                System.err.println(String.format("Actuator status is '%s'", response.getStatus()));
                if (!CollectionUtils.isEmpty(response.getDetails())) {
                    System.err.println("details");
                    for (Map.Entry<String, Object> entry : response.getDetails().entrySet()) {
                        System.err.println(String.format("    %s: '%s'", entry.getKey(), mapper.writeValueAsString(entry.getValue())));
                    }
                }

                System.exit(1);
            }

            throw new RuntimeException("Response code: " + statusCode + ", message: " + readContent(httpResponse));
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static String readContent(HttpResponse httpResponse) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             InputStream is = httpResponse.getEntity().getContent()) {
            byte[] bytes = new byte[1024];
            int read;
            while ((read = is.read(bytes)) != -1) {
                bos.write(bytes, 0, read);
            }

            return bos.toString();
        }
    }
}
