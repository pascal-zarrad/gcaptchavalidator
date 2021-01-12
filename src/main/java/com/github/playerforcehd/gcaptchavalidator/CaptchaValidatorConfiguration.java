/*
 * The MIT License
 *
 * Copyright (c) 2021 Pascal Zarrad
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

import java.util.Map;

/**
 * Represents the basic configuration required to send request to the a site verification server.
 * <p>
 * The necessary configuration consists of the following parameters:
 * - The secret token
 * - The site verification servers URL
 * <p>
 * The default site verification URL points to Googles 'siteverify' URL as defined in the
 * ReCaptcha documentation.
 * The default ReCaptcha version that the library is expecting is version 3.
 * <p>
 * This class consists of fluent setters to allow it to be used like a builder.
 *
 * @author Pascal Zarrad
 * @see <a href="https://developers.google.com/recaptcha/docs/verify#api_request">Google ReCaptcha Documentation</a>
 * @since 3.0.0
 */
public interface CaptchaValidatorConfiguration {
    /**
     * Get the configured secret token
     *
     * @return The configured secret token
     */
    String getSecretToken();

    /**
     * Set the secret token
     *
     * @param secretToken Th secret token to set
     * @return This VerifierConfiguration instance
     */
    CaptchaValidatorConfiguration setSecretToken(String secretToken);

    /**
     * Get the verification URL used to verify responses
     *
     * @return The verification URL used for verification
     */
    String getVerifierUrl();

    /**
     * Set the verification URL used to verify responses
     *
     * @param verifierUrl The new verification URL used for verification
     * @return This VerifierConfiguration instance
     */
    CaptchaValidatorConfiguration setVerifierUrl(String verifierUrl);

    /**
     * Get the custom HTTP headers to send using this configuration.
     *
     * @return The HTTP headers to send when using this configuration
     */
    Map<String, String> getHttpHeaders();

    /**
     * Set the HTTP headers to send when using this configuration
     * <p>
     * Note that this will override the default headers that are sent by GCaptchaValidator.
     *
     * @param httpHeaders The HTTP headers to send when using this configuration
     * @return This VerifierConfiguration instance
     */
    CaptchaValidatorConfiguration setHttpHeaders(Map<String, String> httpHeaders);
}
