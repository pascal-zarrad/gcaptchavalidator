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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable verifier configuration.
 * <p>
 * Instances of this class do not allow to change the configuration.
 * The setters of the class will do nothing, except returning the current instance.
 * <p>
 * {@inheritDoc}
 *
 * @author Pascal Zarrad
 * @see CaptchaValidatorConfiguration
 * @since 3.0.0
 */
public class ImmutableValidatorConfiguration implements CaptchaValidatorConfiguration {
    /**
     * The ReCaptcha server side secret required for validation.
     */
    private final String secretToken;

    /**
     * The URL where the verification request is send to
     */
    private final String verifierUrl;

    /**
     * The HTTP headers to append/overwrite on requests using this configuration
     */
    private final Map<String, String> httpHeaders;

    /**
     * Constructor
     *
     * @param captchaValidatorConfiguration The {@link CaptchaValidatorConfiguration} to make immutable
     */
    ImmutableValidatorConfiguration(CaptchaValidatorConfiguration captchaValidatorConfiguration) {
        this.secretToken = captchaValidatorConfiguration.getSecretToken();
        this.verifierUrl = captchaValidatorConfiguration.getVerifierUrl();
        this.httpHeaders = Collections.unmodifiableMap(captchaValidatorConfiguration.getHttpHeaders());
    }

    @Override
    public String getSecretToken() {
        return secretToken;
    }

    @Override
    public ImmutableValidatorConfiguration setSecretToken(String secretToken) {
        return this;
    }

    @Override
    public String getVerifierUrl() {
        return verifierUrl;
    }

    @Override
    public ImmutableValidatorConfiguration setVerifierUrl(String verifierUrl) {
        return this;
    }

    @Override
    public Map<String, String> getHttpHeaders() {
        return this.httpHeaders;
    }

    @Override
    public CaptchaValidatorConfiguration setHttpHeaders(Map<String, String> httpHeaders) {
        return this;
    }
}
