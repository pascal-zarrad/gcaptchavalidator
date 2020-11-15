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

package com.github.playerforcehd.gcaptchavalidator;

import com.github.playerforcehd.gcaptchavalidator.data.ReCaptchaVersion;

import java.util.Map;

/**
 * Mutable verifier configuration.
 *
 * Instances of this class allow the configuration to be changed after instantiation.
 *
 * {@inheritDoc }
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see CaptchaValidatorConfiguration
 */
public class ValidatorConfiguration implements CaptchaValidatorConfiguration {
    /**
     * The ReCaptcha server side secret required for validation.
     */
    private String secretToken;

    /**
     * The targeted ReCaptcha version which is used to decide between different
     * implementations. This allows support for all current available ReCaptcha versions
     * without sacrificing features like the score in version 3.
     */
    private ReCaptchaVersion reCaptchaVersion;

    /**
     * The URL where the verification request is send to
     */
    private String verifierUrl;

    /**
     * The HTTP headers to append/overwrite on requests using this configuration
     */
    private Map<String, String> httpHeaders;

    /**
     * Constructor
     *
     * @param secretToken The secret ReCaptcha site verification token to use to identify on Google's side
     * @param reCaptchaVersion The targeted ReCaptcha version
     * @param verifierUrl The URL where the requests will be send to
     * @param httpHeaders The HTTP headers to send with requests using this configuration
     */
    public ValidatorConfiguration(
        String secretToken,
        ReCaptchaVersion reCaptchaVersion,
        String verifierUrl,
        Map<String, String> httpHeaders
    ) {
        this.secretToken = secretToken;
        this.reCaptchaVersion = reCaptchaVersion;
        this.verifierUrl = verifierUrl;
        this.httpHeaders = httpHeaders;
    }

    @Override
    public String getSecretToken() {
        return secretToken;
    }

    @Override
    public ValidatorConfiguration setSecretToken(String secretToken) {
        this.secretToken = secretToken;

        return  this;
    }

    @Override
    public ReCaptchaVersion getReCaptchaVersion() {
        return reCaptchaVersion;
    }

    @Override
    public ValidatorConfiguration setReCaptchaVersion(ReCaptchaVersion reCaptchaVersion) {
        this.reCaptchaVersion = reCaptchaVersion;

        return this;
    }

    @Override
    public String getVerifierUrl() {
        return verifierUrl;
    }

    @Override
    public ValidatorConfiguration setVerifierUrl(String verifierUrl) {
        this.verifierUrl = verifierUrl;

        return this;
    }

    @Override
    public Map<String, String> getHttpHeaders() {
        return this.httpHeaders;
    }

    @Override
    public CaptchaValidatorConfiguration setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;

        return this;
    }
}
