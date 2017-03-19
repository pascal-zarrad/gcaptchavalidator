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
import com.github.playerforcehd.gcaptchavalidator.util.Callback;
import lombok.Cleanup;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * A request which validates a captcha response on Google SiteVerify servers.
 * To create a {@link CaptchaValidationRequest} use the {@link com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator} class
 * and it's static methods.
 *
 * @author PlayerForceHD
 * @version 1.3.0
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
    private ExecutorService requestPool;

    public CaptchaValidationRequest(CaptchaValidationConfiguration captchaValidationConfiguration, ExecutorService requestPool) {
        this.captchaValidationConfiguration = captchaValidationConfiguration;
        this.requestPool = requestPool;
    }

    /**
     * Fetch a response and check if it is valid.
     * The connection will be established asynchronously, but is re-synchronized through the future
     *
     * @param response The response of a client which will be checked for it's validity.
     * @return A future that will receive the result of the validation.
     * @throws IOException                Thrown when an IO exception occurs while contacting Google's servers
     * @throws CaptchaValidationException Thrown when the parameters of the {@link CaptchaValidationConfiguration} are invalid
     * @since 1.3.0
     */
    public Future<CaptchaValidationResult> fetch(final String response) throws IOException, CaptchaValidationException {
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
     * Fetch a response and check if it is valid.
     * The result will be available in the fail and invoke methods of the callback.
     * Unlike the {@link #fetch(String)} method where the result can be gained synchronously by using {@link Future#get()}, this method will invoke the callback asynchronously
     *
     * @param response                The response of a client which will be checked for it's validity.
     * @param captchaValidationResult The callback that will receive the result (using a callback interface instead of thew java consumer for programs written in Java 7 or so) asynchronous.
     */
    public void fetch(final String response, final Callback<CaptchaValidationResult> captchaValidationResult) {
        this.requestPool.execute(new Runnable() {
            @Override
            public void run() {
                //Parse the parameters from the CaptchaValidationConfiguration
                if (captchaValidationConfiguration.getSecret() == null) {
                    captchaValidationResult.fail(new CaptchaValidationException("The value 'secret' has not been set in the CaptchaValidationConfiguration"));
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
                try {
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
                    captchaValidationResult.invoke(CaptchaValidationResult.deserializeJSon(googleResponse));
                } catch (IOException e) {
                    captchaValidationResult.fail(e);
                }
            }
        });
    }

    /**
     * Fetches the validation asynchronous by calling the {@link #fetchSync()} method from an asynchronous thread.
     *
     * @param callback The callback which will receive the result
     * @deprecated Deprecated since 1.3.0: Use {@link #fetch(String, Callback)} that returns a Future instead of the direct return. This method could be removed in future versions.
     */
    @Deprecated
    public void fetchAsync(final Callback<CaptchaValidationResult> callback) {
        this.requestPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CaptchaValidationResult result = fetchSync();
                    callback.invoke(result);
                } catch (IOException | CaptchaValidationException e) {
                    e.printStackTrace();
                    callback.fail(e);
                }
            }
        });
    }

    /**
     * Fetches the validation asynchronous by calling the {@link #fetchSync()} method from an asynchronous thread.
     *
     * @param callback The callback which will receive the result
     * @param response The new response value which will set the in the {@link CaptchaValidationConfiguration}
     * @deprecated Deprecated since 1.3.0: Use {@link #fetch(String, Callback)} that returns a Future instead of the direct return. This method could be removed in future versions.
     */
    @Deprecated
    public void fetchAsync(final Callback<CaptchaValidationResult> callback, final String response) {
        this.requestPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    CaptchaValidationResult result = fetchSync(response);
                    callback.invoke(result);
                } catch (IOException | CaptchaValidationException e) {
                    e.printStackTrace();
                    callback.fail(e);
                }
            }
        });
    }

    /**
     * Fetch a result from the Google SiteVerify servers by sending a post request to them.
     * All parameters will be taken from the {@link CaptchaValidationConfiguration} of the current instance.
     *
     * @return The result of the request on the Google Servers
     * @throws IOException                Thrown when any IO action went wrong (HTTP request IO)
     * @throws CaptchaValidationException Thrown when any needed value is not set in the {@link CaptchaValidationConfiguration}
     * @deprecated Deprecated since 1.3.0: Use {@link #fetch(String)} that returns a Future instead of the direct return. This method could be removed in future versions.
     */
    @Deprecated
    public CaptchaValidationResult fetchSync() throws IOException, CaptchaValidationException {
        //Parse the parameters from the CaptchaValidationConfiguration
        if (this.captchaValidationConfiguration.getSecret() == null) {
            throw new CaptchaValidationException("The value 'secret' has not been set in the CaptchaValidationConfiguration");
        }
        String secret = this.captchaValidationConfiguration.getSecret();
        if (this.captchaValidationConfiguration.getResponse() == null) {
            throw new CaptchaValidationException("The value 'response' has not been set in the CaptchaValidationConfiguration");
        }
        String response = this.captchaValidationConfiguration.getResponse();
        String remoteIP = null;
        if (this.captchaValidationConfiguration.getRemoteIP() != null) {
            remoteIP = this.captchaValidationConfiguration.getRemoteIP();
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

    /**
     * Set the {@link CaptchaValidationConfiguration#response} value and then calls fetchSync();
     * IMPORTANT: A call to this method changes a value of the {@link CaptchaValidationConfiguration}!
     *
     * @param response The new response value which will set the in the {@link CaptchaValidationConfiguration}
     * @return The result of the request on the Google Servers
     * @throws IOException                Thrown when any IO action went wrong (HTTP request IO)
     * @throws CaptchaValidationException Thrown when any needed value is not set in the {@link CaptchaValidationConfiguration}
     * @deprecated Deprecated since 1.3.0: Use {@link #fetch(String)} that returns a Future instead of the direct return. This method could be removed in future versions.
     */
    @Deprecated
    public CaptchaValidationResult fetchSync(String response) throws IOException, CaptchaValidationException {
        this.captchaValidationConfiguration.setResponse(response);
        return fetchSync();
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
