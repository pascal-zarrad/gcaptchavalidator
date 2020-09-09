/*
 * The MIT License
 *
 * Copyright (c) 2020 Pascal Zarrad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
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
 * A builder for easy creation of the {@link CaptchaValidationConfiguration}
 *
 * @author PlayerForceHD
 * @since 1.0.0
 */
public class CaptchaValidationConfigurationBuilder {

    /**
     * The configuration that is build with the current builder instance
     */
    private CaptchaValidationConfiguration captchaValidationConfiguration;

    public CaptchaValidationConfigurationBuilder() {
        this.captchaValidationConfiguration = new CaptchaValidationConfiguration();
    }

    /**
     * Creates a new {@link CaptchaValidationConfigurationBuilder} for easy creation of a {@link CaptchaValidationConfiguration}
     *
     * @return A new builder to make it easy to create a {@link CaptchaValidationConfiguration}
     */
    public static CaptchaValidationConfigurationBuilder builder() {
        return new CaptchaValidationConfigurationBuilder();
    }

    /**
     * Set the shared key between your site and ReCaptcha.
     *
     * @param secret The shared key between your site and ReCaptcha.
     * @return The instance of this builder
     */
    public CaptchaValidationConfigurationBuilder withSecret(String secret) {
        this.captchaValidationConfiguration.setSecret(secret);
        return this;
    }

    /**
     * Optional: Set user's IP address.
     *
     * @param remoteIP The user's IP address.
     * @return The instance of this builder
     */
    public CaptchaValidationConfigurationBuilder withRemoteIP(String remoteIP) {
        this.captchaValidationConfiguration.setRemoteIP(remoteIP);
        return this;
    }

    /**
     * Optional: Set a custom {@link ListeningExecutorService} to use instead of the default one.
     *
     * @param executorService A custom {@link ListeningExecutorService} to use instead of the default one created by GCaptchaValidator
     * @return The instance of this builder
     */
    public CaptchaValidationConfigurationBuilder withExecutorService(ListeningExecutorService executorService) {
        this.captchaValidationConfiguration.setExecutorService(executorService);
        return this;
    }

    /**
     * Set a custom URL where the requests will be sent to
     *
     * @param siteVerifyURL The URL to set as {@link String}
     * @return The instance of this builder
     */
    public CaptchaValidationConfigurationBuilder withGoogleSiteVerifyURL(String siteVerifyURL) {
        this.captchaValidationConfiguration.setSiteVerifyURL(siteVerifyURL);
        return this;
    }

    /**
     * Returns the built configuration
     *
     * @return The configuration built with this builder
     */
    public CaptchaValidationConfiguration build() {
        return captchaValidationConfiguration;
    }

}
