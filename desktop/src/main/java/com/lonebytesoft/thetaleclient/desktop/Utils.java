package com.lonebytesoft.thetaleclient.desktop;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Hamster
 * @since 28.04.2015
 */
public class Utils {

    public static final String CLIENT_ID = "com.lonebytesoft.thetaleclient.sdk-26";

    public static String consoleReadLine(final String prompt) {
        final Console console = System.console();
        if(console == null) {
            System.out.print(prompt);
            return inputRead();
        } else {
            final String result;
            if(prompt == null) {
                result = console.readLine();
            } else {
                result = console.readLine(prompt);
            }
            return result;
        }
    }

    public static String consoleReadLine() {
        return consoleReadLine("");
    }

    public static String consoleReadPassword(final String prompt) {
        final Console console = System.console();
        if(console == null) {
            System.out.print(prompt);
            return inputRead();
        } else {
            final char[] result;
            if(prompt == null) {
                result = console.readPassword();
            } else {
                result = console.readPassword(prompt);
            }
            return result == null ? null : new String(result);
        }
    }

    public static String consoleReadPassword() {
        return consoleReadPassword("");
    }

    private static String inputRead() {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (IOException ignored) {
            return null;
        }
    }

    public static void consoleWrite(final String line) {
        System.out.println(line);
    }

    public static void consoleWriteError(final String line) {
        System.err.println(line);
    }

    public static String getUrl(final String url) {
        final HttpClient httpClient = HttpClients.createDefault();
        final HttpUriRequest httpUriRequest = new HttpGet(url);
        try {
            final HttpEntity httpEntity = httpClient.execute(httpUriRequest).getEntity();
            if(httpEntity == null) {
                return null;
            } else {
                return EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            Utils.consoleWriteError(e.getMessage());
            return null;
        }
    }

}
