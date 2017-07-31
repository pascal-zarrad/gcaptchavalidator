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

package com.github.playerforcehd.gcaptchavalidator;

import com.github.playerforcehd.gcaptchavalidator.captchaconfiguration.CaptchaValidationConfiguration;
import com.github.playerforcehd.gcaptchavalidator.captchaconfiguration.CaptchaValidationConfigurationBuilder;
import com.github.playerforcehd.gcaptchavalidator.captchaverification.CaptchaValidationRequest;
import com.github.playerforcehd.gcaptchavalidator.util.Callback;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class of the GCaptchaValidator library providing quick access to the builders for the
 * {@link com.github.playerforcehd.gcaptchavalidator.captchaconfiguration.CaptchaValidationConfiguration} and {@link CaptchaValidationRequest}.
 * <p>
 * This class also includes the {@link java.util.concurrent.ExecutorService} which handles the async execution of requests
 *
 * @author PlayerForceHD
 * @version 2.0.0
 * @since 1.0.0
 */
public class GCaptchaValidator {

    // ---------- Some Constants which are used for every request and contains the HTTP data ---------- //

    /**
     * The URL of the Google SiteVerify service, which is used to validate ReCaptcha responses.
     */
    public static final String GOOGLE_SITEVERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    /**
     * Defines in which charset the response of the HTTP request is requested
     */
    public static final String HTTP_CHARSET = StandardCharsets.UTF_8.toString();

    // ------------------------------------------------------------------------------------------------ //

    /**
     * The {@link ListeningExecutorService} used to run our requests asynchronous.
     */
    @Getter
    private static final ListeningExecutorService REQUEST_POOL = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    /**
     * A GSon instance to parse the JSon result provided by the Google SiteVerify servers
     */
    @Getter
    private static final Gson GSON = new GsonBuilder().create();

    /**
     * Create a {@link CaptchaValidationRequest} from a {@link CaptchaValidationConfiguration}.
     * Use {@link CaptchaValidationRequest#validate(String)} to
     * validate a request.
     * <p>
     * To gain a {@link CaptchaValidationConfiguration} use the {@link GCaptchaValidator#createConfigurationBuilder()} or {@link CaptchaValidationConfigurationBuilder#builder()} method
     *
     * @param captchaValidationConfiguration The configuration which provides all data needed to validate a request
     * @return The {@link CaptchaValidationRequest} which can be used to validate a Captcha response of a user
     */
    public static CaptchaValidationRequest createRequest(CaptchaValidationConfiguration captchaValidationConfiguration) {
        return new CaptchaValidationRequest(captchaValidationConfiguration, captchaValidationConfiguration.getExecutorService() == null ? REQUEST_POOL : captchaValidationConfiguration.getExecutorService());
    }

    /**
     * Creates a new {@link CaptchaValidationConfigurationBuilder} which makes it easy to create a {@link CaptchaValidationConfiguration}
     * which is needed to create a {@link CaptchaValidationRequest}
     *
     * @return A new {@link CaptchaValidationConfigurationBuilder}
     */
    public static CaptchaValidationConfigurationBuilder createConfigurationBuilder() {
        return CaptchaValidationConfigurationBuilder.builder();
    }
}
