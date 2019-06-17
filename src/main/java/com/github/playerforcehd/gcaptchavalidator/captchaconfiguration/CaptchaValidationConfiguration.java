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

package com.github.playerforcehd.gcaptchavalidator.captchaconfiguration;

import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * The configuration
 *
 * @author PlayerForceHD
 * @version 2.0.0
 * @since 1.0.0
 */
public class CaptchaValidationConfiguration {

    /**
     * The shared key between your site and ReCaptcha.
     */
    private String secret;
    /**
     * Optional: The user's IP address.
     */
    private String remoteIP;
    /**
     * Optional: An custom ExecutorService instead of the default one
     */
    private ListeningExecutorService executorService;

    /**
     * Creates a new {@link CaptchaValidationConfigurationBuilder} for easy creation of a {@link CaptchaValidationConfiguration}
     *
     * @return A new builder to make it easy to create a {@link CaptchaValidationConfiguration}
     */
    public static CaptchaValidationConfigurationBuilder builder() {
        return new CaptchaValidationConfigurationBuilder();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }

    public ListeningExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ListeningExecutorService executorService) {
        this.executorService = executorService;
    }
}
