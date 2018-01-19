/*
 * The MIT License
 *
 * Copyright (c) 2016 Pascal Zarrad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.playerforcehd.gcaptchavalidator.captchaverification;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidationException;
import com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator;
import com.github.playerforcehd.gcaptchavalidator.captchaconfiguration.CaptchaValidationConfiguration;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.Cleanup;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * A request which validates a captcha response on Google SiteVerify servers.
 * To create a {@link CaptchaValidationRequest} use the {@link com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator} class
 * and it's static methods.
 *
 * @author PlayerForceHD
 * @version 2.0.0
 * @since 1.0.0
 */
public class CaptchaValidationRequest {

    /**
     * The {@link com.github.playerforcehd.gcaptchavalidator.captchaconfiguration.CaptchaValidationConfiguration} which contains the POST data for the HTTP-Request
     */
    @Getter
    private CaptchaValidationConfiguration captchaValidationConfiguration;

    /**
     * A {@link ExecutorService} which contains all threads of the asynchronous running of a verification request
     */
    private ListeningExecutorService requestPool;

    public CaptchaValidationRequest(CaptchaValidationConfiguration captchaValidationConfiguration, ListeningExecutorService requestPool) {
        this.captchaValidationConfiguration = captchaValidationConfiguration;
        this.requestPool = requestPool;
    }

    /**
     * Validate a Google ReCaptcha response on the server side
     *
     * @param response The Response to to validate (Received over POST parameter 'g-recaptcha-response' when using ReCaptcha in the Web)
     * @return A {@link CaptchaValidationResult} which contains the result of the validation request send to Google's SiteVerify Servers
     */
    public ListenableFuture<CaptchaValidationResult> validate(final String response) {
        return this.requestPool.submit(new Callable<CaptchaValidationResult>() {
            @Override
            public CaptchaValidationResult call() throws Exception {
                //Parse the parameters from the CaptchaValidationConfiguration
                if (captchaValidationConfiguration.getSecret() == null) {
                    throw new CaptchaValidationException("The value 'secret' has not been set in the CaptchaValidationConfiguration");
                }
                String secret = captchaValidationConfiguration.getSecret();
                String remoteIP = null;
                if (captchaValidationConfiguration.getRemoteIP() != null) {
                    remoteIP = captchaValidationConfiguration.getRemoteIP();
                }
                //Create HttpUrlConnection to the Google SiteVerify servers
                Map<String, Object> params = new LinkedHashMap<>();
                params.put("secret", secret);
                params.put("response", response);
                if (remoteIP != null) {
                    params.put("remoteip", remoteIP);
                }
                byte[] parsedParams = createPostData(params);
                URL url = new URL(GCaptchaValidator.GOOGLE_SITEVERIFY_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parsedParams.length));
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.getOutputStream().write(parsedParams);
                @Cleanup BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                for (int c; (c = bufferedReader.read()) >= 0; )
                    stringBuilder.append((char) c);
                String googleResponse = stringBuilder.toString();
                return CaptchaValidationResult.deserializeJSon(googleResponse);
            }
        });
    }

    /**
     * Fast and easy way to add a Callback to a {@link ListenableFuture}
     *
     * @param future   The future which will receive the Callback
     * @param callback The Callback which will be added to the given future
     */
    public void addFutureCallback(ListenableFuture<CaptchaValidationResult> future, FutureCallback<CaptchaValidationResult> callback) {
        Futures.addCallback(future, callback, this.captchaValidationConfiguration.getExecutorService() == null ? this.requestPool : this.captchaValidationConfiguration.getExecutorService());
    }

    /**
     * Create the post request parameters from a map
     *
     * @param params The parameters to parse
     * @return The parsed parameters as a byte array
     * @throws UnsupportedEncodingException Thrown when the UTF-8 encoding is not supported
     */
    private byte[] createPostData(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), GCaptchaValidator.HTTP_CHARSET));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), GCaptchaValidator.HTTP_CHARSET));
        }
        return postData.toString().getBytes(GCaptchaValidator.HTTP_CHARSET);
    }
}
