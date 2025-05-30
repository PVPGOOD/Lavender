package io.justme.lavender.utility.system;

import lombok.experimental.UtilityClass;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@UtilityClass
public class HttpUtility {

    public String get(String urlStr, int connectTimeoutMillis, int readTimeoutMillis, Map<String, String> headers) throws IOException {
        URL url = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(connectTimeoutMillis);
        connection.setReadTimeout(readTimeoutMillis);

        connection.setRequestProperty("Connection", "close");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (LavenderBot)");
        connection.setRequestProperty("Accept-Encoding", "gzip");

        if (headers != null) {
            headers.forEach(connection::setRequestProperty);
        }

        InputStream inputStream;
        if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
            inputStream = new GZIPInputStream(connection.getInputStream());
        } else {
            inputStream = connection.getInputStream();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[8192];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        } finally {
            connection.disconnect();
        }
    }
}
