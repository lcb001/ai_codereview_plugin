package com.lcb.ai_codereview_plugin.review;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AiCodeReviewConfigTest {

    @Test
    public void validatesMissingApiKey() {
        AiCodeReviewConfig config = new AiCodeReviewConfig("https://example.com/v1/chat/completions", "", "model", "");

        assertEquals("API Key is required.", config.validate());
    }

    @Test
    public void acceptsCompleteConfiguration() {
        AiCodeReviewConfig config = new AiCodeReviewConfig("https://example.com/v1/chat/completions", "key", "model", "");

        assertEquals("", config.validate());
    }
}
