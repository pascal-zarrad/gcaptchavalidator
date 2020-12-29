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

package com.github.playerforcehd.gcaptchavalidator.serialize;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidationResponse;

/**
 * An interface that defines a response deserializer that deserializes
 * the JSON received from the Google ReCaptche SiteVerify API into
 * a {@link com.github.playerforcehd.gcaptchavalidator.CaptchaValidationResponse}.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public interface ResponseDeserializer {

    /**
     * Deserialize a response that is supplied as the original String that the SiteVerify API
     * responded with.
     *
     * @param response The response to deserialize
     * @return The deserialized response as an POJO
     */
    CaptchaValidationResponse deserialize(String response);

}
