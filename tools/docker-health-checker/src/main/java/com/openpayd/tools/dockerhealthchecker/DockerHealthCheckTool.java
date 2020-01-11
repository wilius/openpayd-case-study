package com.openpayd.tools.dockerhealthchecker;

import com.openpayd.commons.httpclient.ExchangeHttpClient;
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

public class DockerHealthCheckTool {
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

            HttpResponse httpResponse;
            httpResponse = client.execute(new HttpGet(url));
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                System.exit(0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
}