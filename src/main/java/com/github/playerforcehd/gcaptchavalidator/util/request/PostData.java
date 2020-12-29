package com.github.playerforcehd.gcaptchavalidator.util.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
     * @param params The parameters to parse
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
