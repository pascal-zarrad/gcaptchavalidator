package com.github.playerforcehd.gcaptchavalidator.request;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidatorConfiguration;
import com.github.playerforcehd.gcaptchavalidator.util.request.PostData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The default implementation of a {@link CaptchaRequestHandler} that uses a simple HttpUrlConnection
 * for communication.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class SiteVerifyCaptchaRequestHandler implements CaptchaRequestHandler {
    /**
     * Default charset used by this utility.
     */
    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.toString();

    @Override
    public String request(
        CaptchaValidatorConfiguration captchaValidatorConfiguration,
        String response,
        String remoteIP
    ) throws CaptchaRequestHandlerException {
        // Create map with parameters
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("secret", captchaValidatorConfiguration.getSecretToken());
        params.put("response", response);
        if (remoteIP != null && !remoteIP.isEmpty()) {
            params.put("remoteip", remoteIP);
        }

        try {
            // Prepare post data from Map
            byte[] parsedParams = PostData.createPostData(params, DEFAULT_CHARSET);

            // Execute request
            URL url = new URL(captchaValidatorConfiguration.getVerifierUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.addRequestProperty("Content-Length", String.valueOf(parsedParams.length));
            captchaValidatorConfiguration.getHttpHeaders().forEach(httpURLConnection::addRequestProperty);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.getOutputStream().write(parsedParams);
            StringBuilder stringBuilder = new StringBuilder();
            try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream(),
                    DEFAULT_CHARSET
                ))
            ) {
                for (int c; (c = bufferedReader.read()) >= 0; ) {
                    stringBuilder.append((char) c);
                }
            } catch (IOException e) {
                // Fallback to error stream and if that fails, fallback to outer catch block
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getErrorStream(),
                    DEFAULT_CHARSET
                ));

                for (int c; (c = bufferedReader.read()) >= 0; ) {
                    stringBuilder.append((char) c);
                }
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            throw new CaptchaRequestHandlerException(e);
        }
    }
}
