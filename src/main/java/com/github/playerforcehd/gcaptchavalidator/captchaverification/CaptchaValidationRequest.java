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
import lombok.Getter;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * A request which validates a captcha response on Google SiteVerify servers.
 * To create a {@link CaptchaValidationRequest} use the {@link com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator} class
 * and it's static methods.
 *
 * @author PlayerForceHD
 * @version 1.0.0
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
     * Fetches the validation asynchronous by calling the {@link #fetchSync()} method from an asynchronous thread.
     *
     * @param callback The callback which will receive the result
     */
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
     */
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
     */
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
        //Create HttpClient to the Google SiteVerify servers
        HttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpPost postData = new HttpPost(GCaptchaValidator.GOOGLE_SITEVERIFY_URL);
        postData.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("secret", secret));
        parameters.add(new BasicNameValuePair("response", response));
        if (remoteIP != null) {
            parameters.add(new BasicNameValuePair("remoteip", remoteIP));
        }
        postData.setEntity(new UrlEncodedFormEntity(parameters));
        HttpResponse httpResponse = httpClient.execute(postData);
        BufferedReader resultReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        StringBuilder jsonResult = new StringBuilder();
        String line;
        while ((line = resultReader.readLine()) != null) {
            jsonResult.append(line);
        }
        resultReader.close();
        return CaptchaValidationResult.deserializeJSon(jsonResult.toString());
    }

    /**
     * Set the {@link CaptchaValidationConfiguration#response} value and then calls fetchSync();
     * IMPORTANT: A call to this method changes a value of the {@link CaptchaValidationConfiguration}!
     *
     * @param response The new response value which will set the in the {@link CaptchaValidationConfiguration}
     * @return The result of the request on the Google Servers
     * @throws IOException                Thrown when any IO action went wrong (HTTP request IO)
     * @throws CaptchaValidationException Thrown when any needed value is not set in the {@link CaptchaValidationConfiguration}
     */
    public CaptchaValidationResult fetchSync(String response) throws IOException, CaptchaValidationException {
        this.captchaValidationConfiguration.setResponse(response);
        return fetchSync();
    }
}
