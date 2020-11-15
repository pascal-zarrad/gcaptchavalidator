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
 * {@link CaptchaValidationResponse} for validations using the old version 2 standard.
 *
 * The type is equal to the base {@link CaptchaValidationResponse} as version 3 is only an extension
 * of version 2's responses.
 *
 * An instantiated {@link CaptchaValidationResponseVersion2} is always immutable.
 *
 * The documentation about ReCaptcha version 2 validation can be found here:
 * <a href="https://developers.google.com/recaptcha/docs/verify">Google ReCaptcha v2 Documentation</a>
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see com.github.playerforcehd.gcaptchavalidator.CaptchaValidationResponse
 */
public interface CaptchaValidationResponseVersion2 extends CaptchaValidationResponse {}
