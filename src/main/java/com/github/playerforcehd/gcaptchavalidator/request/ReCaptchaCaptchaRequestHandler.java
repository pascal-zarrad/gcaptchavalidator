package com.github.playerforcehd.gcaptchavalidator.request;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidatorConfiguration;
import com.github.playerforcehd.gcaptchavalidator.util.request.PostData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class ReCaptchaCaptchaRequestHandler implements CaptchaRequestHandler {
    /**
     * Default charset used by this utility.
     */
    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.toString();

    @Override
    public String request(
        CaptchaValidatorConfiguration captchaValidatorConfiguration,
        String response
    ) throws CaptchaRequestHandlerException {
        return this.request(captchaValidatorConfiguration, response, "");
    }

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
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parsedParams.length));
            captchaValidatorConfiguration.getHttpHeaders().forEach(httpURLConnection::setRequestProperty);
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
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            throw new CaptchaRequestHandlerException(e);
        }
    }
}
