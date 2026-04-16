package com.lcb.ai_codereview_plugin.review;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OpenAiChatRequestBuilderTest {

    @Test
    public void buildsOpenAiCompatibleRequestJson() {
        String json = new OpenAiChatRequestBuilder().build("test-model", "review this diff");

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        assertEquals("test-model", root.get("model").getAsString());
        assertEquals(0.2, root.get("temperature").getAsDouble(), 0.001);
        assertEquals("system", root.getAsJsonArray("messages").get(0).getAsJsonObject().get("role").getAsString());
        assertEquals("review this diff", root.getAsJsonArray("messages").get(1).getAsJsonObject().get("content").getAsString());
    }
}
