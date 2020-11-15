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

/**
 * {@link CaptchaValidationResponse} for version 3.
 *
 * The difference to the old version 2 is that ReCaptcha has been extended with a score and action name
 * for better verification.
 * This interface extends the original version 2 api to make the new data available.
 *
 * Mapping of the additional version 3 fields:
 *  - score  - score
 *  - action - action
 *
 * The mapped fields can be accessed through their getters:
 *  - score  - {@link #getScore()}
 *  - action - {@link #getAction()}
 *
 * In addition all data from {@link CaptchaValidationResponse} is available
 * through their getters.
 *
 * An instantiated {@link CaptchaValidationResponse} is immutable.
 *
 * The documentation about ReCaptcha version 3 validation can be found here:
 * <a href="https://developers.google.com/recaptcha/docs/v3">Google ReCaptcha v3 Documentation</a>
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see com.github.playerforcehd.gcaptchavalidator.CaptchaValidationResponse
 */
public interface CaptchaValidationResponseVersion3 extends CaptchaValidationResponse {
    /**
     * Get the resulting score of the validation.
     *
     * @return The validation's score
     */
    float getScore();

    /**
     * Get the name of the action the user has done that was protected by ReCaptcha
     *
     * @return The name of the action the user has done
     */
    String getAction();
}
