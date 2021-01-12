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

package com.github.playerforcehd.gcaptchavalidator.serialize;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidationResponse;
import com.github.playerforcehd.gcaptchavalidator.ValidationResponse;
import com.github.playerforcehd.gcaptchavalidator.data.ClientType;
import com.github.playerforcehd.gcaptchavalidator.data.ReCaptchaVersion;
import com.github.playerforcehd.gcaptchavalidator.data.ValidationError;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;

/**
 * A {@link CaptchaResponseDeserializer} that uses the Google GSon library to deserialize
 * the supplied response string.
 * <p>
 * If you want to use this {@link CaptchaResponseDeserializer} as base for an own one,
 * extend it and override {@link SiteVerifyCaptchaResponseDeserializer#createGson()} to supply
 * a custom {@link Gson} instance that uses an own implementation of the
 * {@link ValidationResponseJsonDeserializer}.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class SiteVerifyCaptchaResponseDeserializer implements CaptchaResponseDeserializer {
    /**
     * The Google GSon instance used by the {@link SiteVerifyCaptchaResponseDeserializer}.
     */
    private final Gson gSon;

    /**
     * Constructor
     */
    public SiteVerifyCaptchaResponseDeserializer() {
        this.gSon = this.createGson();
    }

    @Override
    public CaptchaValidationResponse deserialize(String response) {
        return this.gSon.fromJson(response, ValidationResponse.class);
    }

    /**
     * Create the {@link Gson} instance used by this deserializer
     *
     * @return The created {@link Gson} instance
     */
    protected Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        ValidationResponseJsonDeserializer deserializer = new ValidationResponseJsonDeserializer();
        gsonBuilder.registerTypeAdapter(ValidationResponse.class, deserializer);

        return gsonBuilder.create();
    }

    /**
     * The {@link JsonDeserializer} that is used by GSon to deserialize a response from the
     * Google SiteVerify servers.
     */
    private static class ValidationResponseJsonDeserializer implements JsonDeserializer<ValidationResponse> {

        /**
         * Key of the success property
         */
        private static final String SUCCESS_KEY = "success";

        /**
         * Key of the score property
         */
        private static final String SCORE_KEY = "score";

        /**
         * Key of the action property
         */
        private static final String ACTION_KEY = "action";

        /**
         * Key of the challenge timestamp property
         */
        private static final String CHALLENGE_TIMESTAMP_KEY = "challenge_ts";

        /**
         * Key of the hostname property
         */
        private static final String HOSTNAME_KEY = "hostname";

        /**
         * Key of the apk package property
         */
        private static final String APK_PACKAGE_KEY = "apk_package_name";

        /**
         * Key of the error-codes property
         */
        private static final String ERROR_CODES_KEY = "error-codes";

        @Override
        public ValidationResponse deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
        ) throws JsonParseException {
            JsonObject targetObject = jsonElement.getAsJsonObject();

            // Deserialize response with error
            if (targetObject.has(ERROR_CODES_KEY)) {
                return deserializeWithError(targetObject);
            }

            // Parse version, score and action (all ReCaptcha 3.0 things) if applicable
            ReCaptchaVersion reCaptchaVersion = ReCaptchaVersion.VERSION_2;
            float score = -1f;
            String action = "";
            if (targetObject.has(SCORE_KEY) && targetObject.has(ACTION_KEY)) {
                reCaptchaVersion = ReCaptchaVersion.VERSION_3;
                score = targetObject.get(SCORE_KEY).getAsFloat();
                action = targetObject.get(ACTION_KEY).getAsString();
            }

            // Parse if validation succeeded
            boolean succeeded = targetObject.get(SUCCESS_KEY).getAsBoolean();

            // Parse challenge timestamp
            String challengeTimeStampString = targetObject.get(CHALLENGE_TIMESTAMP_KEY).getAsString();
            Date challengeTimestamp = Date.from(Instant.parse(challengeTimeStampString));

            // Parse platform and hostname/package name
            ClientType clientType = null;
            String hostnameOrPackageName = "";

            if (targetObject.has(HOSTNAME_KEY)) {
                clientType = ClientType.WEB;
                hostnameOrPackageName = targetObject.get(HOSTNAME_KEY).getAsString();
            }

            if (targetObject.has(APK_PACKAGE_KEY)) {
                clientType = ClientType.ANDROID;
                hostnameOrPackageName = targetObject.get(APK_PACKAGE_KEY).getAsString();
            }

            return new ValidationResponse(
                reCaptchaVersion,
                succeeded,
                challengeTimestamp,
                clientType,
                hostnameOrPackageName,
                score, action,
                new ValidationError[0]
            );
        }

        /**
         * Parse a response that has the error-codes set, which means the request had an error.
         *
         * @param targetObject The object of the response to deserialize
         * @return The deserialized response.
         */
        private ValidationResponse deserializeWithError(JsonObject targetObject) {
            JsonArray errorCodeArray = targetObject.getAsJsonArray(ERROR_CODES_KEY);
            String[] errorCodes = new String[errorCodeArray.size()];

            Iterator<JsonElement> errorCodesIterator = errorCodeArray.iterator();
            int index = 0;
            while (errorCodesIterator.hasNext()) {
                errorCodes[index] = errorCodesIterator.next().getAsString();
                index++;
            }

            ValidationError[] errors = new ValidationError[errorCodes.length];
            for (int i = 0; i < errorCodes.length; i++) {
                errors[i] = ValidationError.getValidationErrorByCode(errorCodes[i]);
            }

            return new ValidationResponse(
                ReCaptchaVersion.VERSION_2,
                false,
                null,
                null,
                "",
                -1,
                "",
                errors
            );
        }
    }
}
