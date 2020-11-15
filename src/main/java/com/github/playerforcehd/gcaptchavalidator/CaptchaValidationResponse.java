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

import com.github.playerforcehd.gcaptchavalidator.param.ClientType;
import com.github.playerforcehd.gcaptchavalidator.param.ValidationError;

import java.util.Date;

/**
 * Represents a single response of a request that was send to the siteverify servers.
 *
 * The response contains all currently defined fields as stated in Googles documentation.
 * Although, the mapping of the fields is a bit different to make it easier to work with the
 * received response on developers side.
 *
 * Mapping used by ReCaptcha Validator:
 *  - success          - succeeded
 *  - challenge_ts     - challengeTimestamp
 *  - hostname         - hostnameOrPackageName in conjunction with clientType
 *  - apk_package_name - hostnameOrPackageName in conjunction with clientType
 *  - errors           - errors
 *
 * The mapped fields are accessed through their getters:
 *   - succeeded             - {@link #hasSucceeded()}
 *   - challenge_ts          - {@link #getChallengeTimestamp()}
 *   - clientType            - {@link #getClientType()}
 *   - hostnameOrPackageName - {@link #getHostnameOrPackageName()}
 *   - errors                - {@link #getErrors()}
 *
 *  The mapping for hostname and apk_package_name works as the following:
 *   - If the hostname key is present, {@link #getClientType()} will contain {@link ClientType#WEB }
 *     and {@link #getHostnameOrPackageName()} the hostname value of the response.
 *   - If the apk_package_name key is present, {@link #getClientType()} will contain {@link ClientType#ANDROID }
 *     and {@link #getHostnameOrPackageName()} the android package name value of the response.
 *   - If both values are available, the response won't happen as the request will have been failed
 *     with an exception as the mapping cannot be applied.
 *
 * An instantiated {@link CaptchaValidationResponse} is always immutable.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see <a href="https://developers.google.com/recaptcha/docs/verify#api_request">Google ReCaptcha Documentation</a>
 */
public interface CaptchaValidationResponse {
    /**
     * Check if the validation was successful
     *
     * @return The status if the request was successful
     */
    boolean hasSucceeded();

    /**
     * Get the timestamp when the challenge has been loaded.
     *
     * @return The timestamp when the challenge has been loaded
     */
    Date getChallengeTimestamp();

    /**
     * Get the type of client that requested the validation.
     * The type can be used to determine if the hostnameOrPackage field contains
     * either an hostname or Android package name.
     *
     * @return The type of client that send the request
     */
    ClientType getClientType();

    /**
     * Get the hostname or apk_package_name of the response.
     * Use {@link #getClientType()} to determine if this method will return a
     * hostname or apk_package_name.
     *
     * @return The hostname or apk_package_name of the response
     */
    String getHostnameOrPackageName();

    /**
     * Get the errors that have been set on the response
     *
     * @return The errors that are set on the response
     */
    ValidationError[] getErrors();
}
