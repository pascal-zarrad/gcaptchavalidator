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

/**
 * A enum which contains all possible error codes that the JSon response from Google could contain.
 * For more information look at the Google ReCaptcha 2.0 API documentation
 *
 * @author PlayerForceHD
 * @see <a href="https://developers.google.com/recaptcha/docs/verify#error-code-reference"></a>
 * @since 1.0.0
 */
public enum CaptchaValidationError {

    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate");


    /**
     * The original string from the JSon response from Google
     */
    private String originalJSon;

    CaptchaValidationError(String originalJSon) {
        this.originalJSon = originalJSon;
    }

    /**
     * Get a ErrorCode as it's enum value
     *
     * @param errorCode The error code which should be grabbed
     * @return The enum which represents the errorCode string
     */
    public static CaptchaValidationError parseGoogleJSonErrorCode(String errorCode) {
        for (CaptchaValidationError captchaValidationError : values()) {
            if (captchaValidationError.getOriginalJSon().equals(errorCode)) {
                return captchaValidationError;
            }
        }
        return null;
    }

    public String getOriginalJSon() {
        return originalJSon;
    }
}
