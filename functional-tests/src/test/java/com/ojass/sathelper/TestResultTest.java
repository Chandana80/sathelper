package com.ojass.sathelper;

import com.meterware.httpunit.*;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

@SuppressWarnings({ "unchecked" })
public class TestResultTest {

    @Test
    public void testConnection() throws Exception {
        WebConversation conversation = new WebConversation();
        // should cause 405 if not request parameters provided
        WebRequest request = new GetMethodWebRequest("http://localhost:8080/sathelper/users");
        try {
            conversation.getResponse(request);
            fail("Should respond with 405 (not supported) if no request parameters provided");
        } catch (HttpException e) {
            // ok, we expected that
            assertEquals(e.getResponseCode(), 404);
        }
    }

    @Test
    public void testCreate() throws Exception {
        WebConversation conversation = new WebConversation();
        String inputString = "{\"name\":\"testResult\",\"testDate\":\"2016-09-23\",\"results\":[{\"user\":{\"id\":2},\"subject\":{\"id\":1},\"score\":\"23\"},{\"user\":{\"id\":3},\"subject\":{\"id\":1},\"score\":\"23\"},{\"user\":{\"id\":4},\"subject\":{\"id\":1},\"score\":\"34\"},{\"user\":{\"id\":6},\"subject\":{\"id\":1},\"score\":\"23\"},{\"user\":{\"id\":7},\"subject\":{\"id\":1},\"score\":\"23\"},{\"user\":{\"id\":8},\"subject\":{\"id\":1},\"score\":\"23\"},{\"user\":{\"id\":9},\"subject\":{\"id\":1},\"score\":\"23\"},{\"user\":{\"id\":10},\"subject\":{\"id\":1},\"score\":\"23\"}]}";
        InputStream stream = new ByteArrayInputStream(inputString.getBytes());
        WebRequest request = new PostMethodWebRequest("http://localhost:8080/sathelper/testResults", stream, "application/json");
        request.setHeaderField("Content-Type", "application/json");
        request.setHeaderField("Accept", "*/*");
        request.setHeaderField("charset", "ISO-8859-1");
        request.setHeaderField("user-agent", "Apache-HttpClient/4.3.5 (java 1.8)");
        WebResponse response = conversation.getResponse(request);
        assertEquals(200, response.getResponseCode());
    }

}
