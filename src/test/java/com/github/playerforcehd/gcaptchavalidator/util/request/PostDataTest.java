package com.github.playerforcehd.gcaptchavalidator.util.request;

import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

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
