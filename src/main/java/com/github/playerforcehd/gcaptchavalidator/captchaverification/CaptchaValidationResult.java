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

package com.github.playerforcehd.gcaptchavalidator.captchaverification;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The response from the Google SiteKey servers.
 * Variables are named like the original JSon values but in a Java convention conform format.
 * More information at the Google ReCaptcha 2.0 API documentation
 *
 * @author PlayerForceHD
 * @version 1.0.0
 * @see <a href="http://google.com">https://developers.google.com/recaptcha/docs/verify#api-response</a>
 * @since 1.0.0
 */
public class CaptchaValidationResult {

    /**
     * If the success value is true, the captcha result is real
     */
    @Getter
    private boolean success;

    /**
     * The timestamp of the challenge load (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)
     */
    @Getter
    private String challengeTS;

    /**
     * The hostname of the site where the ReCAPTCHA was solved
     */
    @Getter
    private String hostName;

    /**
     * When success is false, error codes are available in this collection.
     *
     * @see <a href="https://developers.google.com/recaptcha/docs/verify#error-code-reference</a>
     */
    @Getter
    private Collection<CaptchaValidationError> errorCodes;

    public CaptchaValidationResult(boolean success, String challengeTS, String hostName, Collection<CaptchaValidationError> errorCode) {
        this.success = success;
        this.challengeTS = challengeTS;
        this.hostName = hostName;
        this.errorCodes = errorCode;
    }

    /**
     * Deserialize a result from the Google SiteKey servers
     *
     * @param json The JSon to deserialize
     * @return The result of the deserialization
     */
    static CaptchaValidationResult deserializeJSon(String json) {
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        boolean success = jsonObject.get("success").getAsBoolean();
        String challengeTS = null;
        if (jsonObject.get("challenge_ts") != null) {
            challengeTS = jsonObject.get("challenge_ts").getAsString();
        }
        String hostName = null;
        if (jsonObject.get("hostname") != null) {
            hostName = jsonObject.get("hostname").getAsString();
        }
        Collection<CaptchaValidationError> errorCodes = new ArrayList<>();
        if (jsonObject.get("error-codes") != null) {
            JsonElement errorList = jsonObject.get("error-codes");
            for (int i = errorList.getAsJsonArray().size() - 1; i >= 0; i--) {
                errorCodes.add(CaptchaValidationError.parseGoogleJSonErrorCode(errorList.getAsJsonArray().get(i).getAsString()));
            }
        }
        return new CaptchaValidationResult(success, challengeTS, hostName, errorCodes);
    }

    /**
     * Get the validation result of the send request
     *
     * @return The result of the validation
     */
    public boolean isValid() {
        return this.success;
    }
}
