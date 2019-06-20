/*
 * The MIT License
 *
 * Copyright (c) 2019 Pascal Zarrad
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
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * A request which validates a captcha response on Google SiteVerify servers.
 * To create a {@link CaptchaValidationRequest} use the {@link com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator} class
 * and it's static methods.
 *
 * @author PlayerForceHD
 * @since 1.0.0
 */
public class CaptchaValidationRequest {

    /**
     * The {@link com.github.playerforcehd.gcaptchavalidator.captchaconfiguration.CaptchaValidationConfiguration} which contains the POST data for the HTTP-Request
     */
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
        return this.requestPool.submit(() -> {
            if (captchaValidationConfiguration.getSecret() == null) {
                throw new CaptchaValidationException("The value 'secret' has not been set in the CaptchaValidationConfiguration");
            }
            String secret = captchaValidationConfiguration.getSecret();
            String remoteIP = null;
            if (captchaValidationConfiguration.getRemoteIP() != null) {
                remoteIP = captchaValidationConfiguration.getRemoteIP();
            }
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("secret", secret));
            parameters.add(new BasicNameValuePair("remoteip", remoteIP));
            parameters.add(new BasicNameValuePair("response", response));
            String googleResponse;
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                URL url = new URL(captchaValidationConfiguration.getSiteVerifyURL());
                HttpPost httpPost = new HttpPost(url.toURI());
                httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
                httpPost.setEntity(new UrlEncodedFormEntity(parameters));
                try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null) {
                        googleResponse = EntityUtils.toString(httpEntity, GCaptchaValidator.HTTP_CHARSET);
                    } else {
                        throw new CaptchaValidationException("Could not retrieve a body from the request!");
                    }
                }
            }
            if (googleResponse == null) {
                throw new CaptchaValidationException("Received an empty response. This could be due to a failure while communicating with Google or because the returned body was empty!");
            }
            return CaptchaValidationResult.deserializeJSon(googleResponse);
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

    public CaptchaValidationConfiguration getCaptchaValidationConfiguration() {
        return captchaValidationConfiguration;
    }
}
