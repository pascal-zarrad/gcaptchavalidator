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

package com.github.playerforcehd.gcaptchavalidator.data;

/**
 * Provide a list of all possible error codes that a validation response can contain.
 * <p>
 * The specific meaning of the error codes can be looked up on Google documentation.
 * Also provides a method to map the plain error codes to the enum.
 *
 * @author Pascal Zarrad
 * @see <a href="https://developers.google.com/recaptcha/docs/verify#error_code_reference">Google ReCaptcha Errors</a>
 * @since 3.0.0
 */
public enum ValidationError {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
    // Custom error that indicates some issue with reaching the SiteVerify API or a malformed JSon response
    GCAPTCHAVALIDATOR_INTERNAL_ERROR("gcaptchavaliator-internal-error");

    /**
     * The error that has been thrown.
     * Equals the error codes listed in Googles documentation.
     */
    private final String plainError;

    /**
     * Constructor
     *
     * @param plainError The original error string that has been thrown
     */
    ValidationError(String plainError) {
        this.plainError = plainError;
    }

    /**
     * Get a specific validation error by its error code (as defined in the ReCaptcha  configuration).
     *
     * @param plainError The error for which the {@link ValidationError} should be found for
     * @return The found {@link ValidationError} or null
     */
    public static ValidationError getValidationErrorByCode(String plainError) {
        for (ValidationError error : ValidationError.values()) {
            if (error.getPlainError().equals(plainError)) {
                return error;
            }
        }

        return null;
    }

    /**
     * Get the plain error as it was set on the received response of the siteverify servers.
     *
     * @return The error as it was received from the siteverify server
     */
    public String getPlainError() {
        return plainError;
    }
}
