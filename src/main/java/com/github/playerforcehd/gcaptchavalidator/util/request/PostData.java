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

package com.github.playerforcehd.gcaptchavalidator.util.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Utility class that provides functionality to convert a normal {@link java.util.Map} to
 * an byte array that could be passed as post data to an {@link java.net.HttpURLConnection}.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class PostData {
    /**
     * Create the post request parameters from a map
     *
     * @param params  The parameters to parse
     * @param charset Teh charset that the string consists of
     * @return The parsed parameters as a byte array
     * @throws UnsupportedEncodingException Thrown when the UTF-8 encoding is not supported
     */
    public static byte[] createPostData(Map<String, Object> params, String charset) throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), charset));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), charset));
        }
        return postData.toString().getBytes(charset);
    }
}
