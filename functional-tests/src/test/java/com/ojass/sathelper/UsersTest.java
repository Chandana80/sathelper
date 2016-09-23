package com.ojass.sathelper;

import com.meterware.httpunit.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.testng.Assert.*;

@SuppressWarnings({ "unchecked" })
public class UsersTest {

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
        String inputString = "{\"firstName\":\"Chandana9\",\"lastName\":\"Davuluri\",\"email\":\"chandana9.davuluri@abc.com\",\"password\":\"abc123\",\"subjects\":[{\"id\":2}]}";
        InputStream stream = new ByteArrayInputStream(inputString.getBytes());
        WebRequest request = new PostMethodWebRequest("http://localhost:8080/sathelper/register/ROLE_TEACHER", stream, "application/json");
        request.setHeaderField("Content-Type", "application/json");
        request.setHeaderField("Accept", "*/*");
        request.setHeaderField("charset", "ISO-8859-1");
        request.setHeaderField("user-agent", "Apache-HttpClient/4.3.5 (java 1.8)");
        WebResponse response = conversation.getResponse(request);
        assertEquals(201, response.getResponseCode());
        Integer userId = assertResponse(response);

        request = new GetMethodWebRequest("http://localhost:8080/sathelper/users/" + userId);
        response = conversation.getResponse(request);
        assertEquals(200, response.getResponseCode());
        assertResponse(response);
    }

    public Integer assertResponse(WebResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> result = mapper.readValue(response.getText(), Map.class);
        Integer userId = Integer.parseInt(result.get("id"));
        assertNotNull(userId);
        assertEquals("Chandana9", (String)result.get("firstName"));
        assertEquals("Davuluri", (String)result.get("lastName"));
        return userId;
    }

}
