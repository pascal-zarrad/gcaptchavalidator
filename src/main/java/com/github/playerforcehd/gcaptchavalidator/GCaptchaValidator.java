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

import com.github.playerforcehd.gcaptchavalidator.data.ReCaptchaVersion;
import com.github.playerforcehd.gcaptchavalidator.data.ValidationError;
import com.github.playerforcehd.gcaptchavalidator.request.CaptchaRequestHandler;
import com.github.playerforcehd.gcaptchavalidator.request.CaptchaRequestHandlerException;
import com.github.playerforcehd.gcaptchavalidator.request.SiteVerifyCaptchaRequestHandler;
import com.github.playerforcehd.gcaptchavalidator.serialize.CaptchaResponseDeserializer;
import com.github.playerforcehd.gcaptchavalidator.serialize.SiteVerifyCaptchaResponseDeserializer;

import java.util.Map;

/**
 * Default implementation of {@link CaptchaValidator}.
 * <p>
 * This {@link CaptchaValidator} uses the default {@link SiteVerifyCaptchaRequestHandler}
 * and {@link SiteVerifyCaptchaResponseDeserializer} to validate and verify Google ReCaptcha 2.0,
 * Invisible ReCaptcha and ReCaptcha 3.0 responses.
 * The used {@link CaptchaRequestHandler} and {@link CaptchaResponseDeserializer} are immutable but
 * can be replaced with own implementations when a new GCaptchaValidator instance is being created.
 * <p>
 * The CaptchaValidatorConfiguration instance that is used will be immutable as soon as it has been passed
 * as a parameter to this class to prevent concurrent modifications in multi-threaded environments.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class GCaptchaValidator implements CaptchaValidator {
    /**
     * The immutable {@link CaptchaRequestHandler} used by this {@link CaptchaValidator}
     */
    private final CaptchaRequestHandler captchaRequestHandler;

    /**
     * The immutable {@link CaptchaResponseDeserializer} used by this {@link CaptchaValidator}
     */
    private final CaptchaResponseDeserializer captchaResponseDeserializer;

    /**
     * The mutable {@link CaptchaValidatorConfiguration} that defines basic configuration like:
     * - The secret
     * - The URL to the SiteVerify servers
     * - The additional HTTP headers to send
     */
    private final CaptchaValidatorConfiguration captchaValidatorConfiguration;

    // --- Constructors with manual parameters

    /**
     * Constructor
     *
     * @param secret The secret of the Google ReCaptcha API
     */
    public GCaptchaValidator(String secret) {
        this.captchaRequestHandler = new SiteVerifyCaptchaRequestHandler();
        this.captchaResponseDeserializer = new SiteVerifyCaptchaResponseDeserializer();
        this.captchaValidatorConfiguration = new ValidatorConfiguration(secret);
    }

    /**
     * Constructor
     *
     * @param secret                The secret of the Google ReCaptcha API
     * @param captchaRequestHandler The {@link CaptchaRequestHandler} to use when creating requests
     */
    public GCaptchaValidator(
        String secret,
        CaptchaRequestHandler captchaRequestHandler
    ) {
        this.captchaRequestHandler = captchaRequestHandler;
        this.captchaResponseDeserializer = new SiteVerifyCaptchaResponseDeserializer();
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(new ValidatorConfiguration(secret));
    }

    /**
     * Constructor
     *
     * @param secret                      The secret of the Google ReCaptcha API
     * @param captchaResponseDeserializer The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        String secret,
        CaptchaResponseDeserializer captchaResponseDeserializer
    ) {
        this.captchaRequestHandler = new SiteVerifyCaptchaRequestHandler();
        this.captchaResponseDeserializer = captchaResponseDeserializer;
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(new ValidatorConfiguration(secret));
    }

    /**
     * Constructor
     *
     * @param secret                      The secret of the Google ReCaptcha API
     * @param captchaRequestHandler       The {@link CaptchaRequestHandler} to use when creating requests
     * @param captchaResponseDeserializer The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        String secret,
        CaptchaRequestHandler captchaRequestHandler,
        CaptchaResponseDeserializer captchaResponseDeserializer
    ) {
        this.captchaRequestHandler = captchaRequestHandler;
        this.captchaResponseDeserializer = captchaResponseDeserializer;
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(new ValidatorConfiguration(secret));
    }

    /**
     * Constructor
     *
     * @param secret        The secret of the Google ReCaptcha API
     * @param siteVerifyUrl The site verify url to use
     */
    public GCaptchaValidator(String secret, String siteVerifyUrl) {
        this.captchaRequestHandler = new SiteVerifyCaptchaRequestHandler();
        this.captchaResponseDeserializer = new SiteVerifyCaptchaResponseDeserializer();
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(
            new ValidatorConfiguration(secret, siteVerifyUrl)
        );
    }

    /**
     * Constructor
     *
     * @param secret                The secret of the Google ReCaptcha API
     * @param siteVerifyUrl         The site verify url to use
     * @param captchaRequestHandler The {@link CaptchaRequestHandler} to use when creating requests
     */
    public GCaptchaValidator(
        String secret,
        String siteVerifyUrl,
        CaptchaRequestHandler captchaRequestHandler
    ) {
        this.captchaRequestHandler = captchaRequestHandler;
        this.captchaResponseDeserializer = new SiteVerifyCaptchaResponseDeserializer();
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(
            new ValidatorConfiguration(secret, siteVerifyUrl)
        );
    }

    /**
     * Constructor
     *
     * @param secret                      The secret of the Google ReCaptcha API
     * @param siteVerifyUrl               The site verify url to use
     * @param captchaResponseDeserializer The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        String secret,
        String siteVerifyUrl,
        CaptchaResponseDeserializer captchaResponseDeserializer
    ) {
        this.captchaRequestHandler = new SiteVerifyCaptchaRequestHandler();
        this.captchaResponseDeserializer = captchaResponseDeserializer;
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(
            new ValidatorConfiguration(secret, siteVerifyUrl)
        );
    }

    /**
     * Constructor
     *
     * @param secret                      The secret of the Google ReCaptcha API
     * @param siteVerifyUrl               The site verify url to use
     * @param captchaRequestHandler       The {@link CaptchaRequestHandler} to use when creating requests
     * @param captchaResponseDeserializer The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        String secret,
        String siteVerifyUrl,
        CaptchaRequestHandler captchaRequestHandler,
        CaptchaResponseDeserializer captchaResponseDeserializer
    ) {
        this.captchaRequestHandler = captchaRequestHandler;
        this.captchaResponseDeserializer = captchaResponseDeserializer;
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(
            new ValidatorConfiguration(secret, siteVerifyUrl)
        );
    }

    /**
     * Constructor
     *
     * @param secret        The secret of the Google ReCaptcha API
     * @param siteVerifyUrl The site verify url to use
     * @param httpHeaders   Custom http headers to send with the request (Danger, overrides default headers!)
     */
    public GCaptchaValidator(String secret, String siteVerifyUrl, Map<String, String> httpHeaders) {
        this.captchaRequestHandler = new SiteVerifyCaptchaRequestHandler();
        this.captchaResponseDeserializer = new SiteVerifyCaptchaResponseDeserializer();
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(
            new ValidatorConfiguration(secret, siteVerifyUrl, httpHeaders)
        );
    }

    /**
     * Constructor
     *
     * @param secret                The secret of the Google ReCaptcha API
     * @param siteVerifyUrl         The site verify url to use
     * @param httpHeaders           Custom http headers to send with the request (Danger, overrides default headers!)
     * @param captchaRequestHandler The {@link CaptchaRequestHandler} to use when creating requests
     */
    public GCaptchaValidator(
        String secret,
        String siteVerifyUrl,
        Map<String, String> httpHeaders,
        CaptchaRequestHandler captchaRequestHandler
    ) {
        this.captchaRequestHandler = captchaRequestHandler;
        this.captchaResponseDeserializer = new SiteVerifyCaptchaResponseDeserializer();
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(
            new ValidatorConfiguration(secret, siteVerifyUrl, httpHeaders)
        );
    }

    /**
     * Constructor
     *
     * @param secret                      The secret of the Google ReCaptcha API
     * @param siteVerifyUrl               The site verify url to use
     * @param httpHeaders                 Custom http headers to send with the request (Danger, overrides default headers!)
     * @param captchaResponseDeserializer The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        String secret,
        String siteVerifyUrl,
        Map<String, String> httpHeaders,
        CaptchaResponseDeserializer captchaResponseDeserializer
    ) {
        this.captchaRequestHandler = new SiteVerifyCaptchaRequestHandler();
        this.captchaResponseDeserializer = captchaResponseDeserializer;
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(
            new ValidatorConfiguration(secret, siteVerifyUrl, httpHeaders)
        );
    }

    /**
     * Constructor
     *
     * @param secret                      The secret of the Google ReCaptcha API
     * @param siteVerifyUrl               The site verify url to use
     * @param httpHeaders                 Custom http headers to send with the request (Danger, overrides default headers!)
     * @param captchaRequestHandler       The {@link CaptchaRequestHandler} to use when creating requests
     * @param captchaResponseDeserializer The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        String secret,
        String siteVerifyUrl,
        Map<String, String> httpHeaders,
        CaptchaRequestHandler captchaRequestHandler,
        CaptchaResponseDeserializer captchaResponseDeserializer
    ) {
        this.captchaRequestHandler = captchaRequestHandler;
        this.captchaResponseDeserializer = captchaResponseDeserializer;
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(
            new ValidatorConfiguration(secret, siteVerifyUrl, httpHeaders)
        );
    }

    // --- Constructors with CaptchaValidatorConfiguration parameter

    /**
     * Constructor
     *
     * @param captchaValidatorConfiguration The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(CaptchaValidatorConfiguration captchaValidatorConfiguration) {
        this.captchaRequestHandler = new SiteVerifyCaptchaRequestHandler();
        this.captchaResponseDeserializer = new SiteVerifyCaptchaResponseDeserializer();
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(captchaValidatorConfiguration);
    }

    /**
     * Constructor
     *
     * @param captchaRequestHandler         The {@link CaptchaRequestHandler} to use when creating requests
     * @param captchaValidatorConfiguration The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        CaptchaRequestHandler captchaRequestHandler,
        CaptchaValidatorConfiguration captchaValidatorConfiguration
    ) {
        this.captchaRequestHandler = captchaRequestHandler;
        this.captchaResponseDeserializer = new SiteVerifyCaptchaResponseDeserializer();
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(captchaValidatorConfiguration);
    }

    /**
     * Constructor
     *
     * @param captchaResponseDeserializer   The {@link CaptchaResponseDeserializer} used for deserialization of responses
     * @param captchaValidatorConfiguration The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        CaptchaResponseDeserializer captchaResponseDeserializer,
        CaptchaValidatorConfiguration captchaValidatorConfiguration
    ) {
        this.captchaRequestHandler = new SiteVerifyCaptchaRequestHandler();
        this.captchaResponseDeserializer = captchaResponseDeserializer;
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(captchaValidatorConfiguration);
    }

    /**
     * Constructor
     *
     * @param captchaRequestHandler         The {@link CaptchaRequestHandler} to use when creating requests
     * @param captchaResponseDeserializer   The {@link CaptchaResponseDeserializer} used for deserialization of responses
     * @param captchaValidatorConfiguration The {@link CaptchaValidatorConfiguration} to use
     */
    public GCaptchaValidator(
        CaptchaRequestHandler captchaRequestHandler,
        CaptchaResponseDeserializer captchaResponseDeserializer,
        CaptchaValidatorConfiguration captchaValidatorConfiguration
    ) {
        this.captchaRequestHandler = captchaRequestHandler;
        this.captchaResponseDeserializer = captchaResponseDeserializer;
        this.captchaValidatorConfiguration = new ImmutableValidatorConfiguration(captchaValidatorConfiguration);
    }

    /**
     * Validate a Google ReCaptcha response.
     * The result if the response is valid is being returned as a boolean.
     *
     * @param response The response to validate
     * @return The result of the external validation as a simple boolean
     */
    @Override
    public boolean basicValidate(String response) {
        return basicValidate(response, "");
    }

    /**
     * Validate a Google ReCaptcha response.
     * The result if the response is valid is being returned as a boolean.
     *
     * @param response The response to validate
     * @param remoteIP The remote IP of the one who issued the request
     * @return The result of the external validation as a simple boolean
     */
    @Override
    public boolean basicValidate(String response, String remoteIP) {
        return validate(response, remoteIP).hasSucceeded();
    }

    @Override
    public CaptchaValidationResponse validate(String response) {
        return validate(response, "");
    }

    /**
     * Validate a Google ReCaptcha response.
     * The result if the response is valid is being returned as a {@link CaptchaValidationResponse}
     * that allows a rich processing of the result afterwards.
     * The {@link CaptchaValidationResponse} will contain the entire result of the validation
     * in a more structured manner for developers.
     *
     * @param response The response to validate
     * @param remoteIP The remote IP of the one who issued the request
     * @return The result of the external validation as a {@link CaptchaValidationResponse}
     */
    @Override
    public CaptchaValidationResponse validate(String response, String remoteIP) {
        try {
            String requestResponse = this.captchaRequestHandler.request(
                this.captchaValidatorConfiguration,
                response,
                remoteIP
            );

            return this.captchaResponseDeserializer.deserialize(requestResponse);
        } catch (CaptchaRequestHandlerException e) {
            return new ValidationResponse(
                ReCaptchaVersion.VERSION_2,
                false,
                null,
                null,
                "",
                -1f,
                "",
                new ValidationError[]{ValidationError.GCAPTCHAVALIDATOR_INTERNAL_ERROR}
            );
        }
    }

    /**
     * Get the {@link CaptchaValidatorConfiguration} that is currently used by this {@link CaptchaValidator}
     *
     * @return The {@link CaptchaValidatorConfiguration} used by this {@link CaptchaValidator}
     */
    @Override
    public CaptchaValidatorConfiguration getConfiguration() {
        return this.captchaValidatorConfiguration;
    }
}
