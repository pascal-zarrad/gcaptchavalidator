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

import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Test the PostData util to ensure its working as intended
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class PostDataTest {
    @Test
    public void testCreatePostData() {
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("Test1", "Value1");
        inputData.put("Test2", "Value2");

        byte[] expected = {
            84, 101, 115, 116, 49,
            61, 86, 97, 108, 117,
            101, 49, 38, 84, 101,
            115, 116, 50, 61, 86,
            97, 108, 117, 101, 50
        };

        try {
            byte[] result = PostData.createPostData(inputData, StandardCharsets.UTF_8.toString());

            assertEquals(result, expected);
        } catch (UnsupportedEncodingException e) {
            fail("PostData#createPostData threw an exception when being called properly", e);
        }
    }

    @Test(expectedExceptions = UnsupportedEncodingException.class)
    public void testCreatePostDataWithException() throws UnsupportedEncodingException {
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("Test1", "Value1");
        inputData.put("Test2", "Value2");

        PostData.createPostData(inputData, "12345Charset12345");
    }
}
