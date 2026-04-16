package com.lcb.ai_codereview_plugin.review;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ReviewPromptBuilderTest {

    @Test
    public void keepsOutputProtocolWhenCustomPromptIsProvided() {
        String message = new ReviewPromptBuilder().buildUserMessage("Only check security issues.", "diff text");

        assertTrue(message.contains("Only check security issues."));
        assertTrue(message.contains("REVIEW_STATUS: PASS"));
        assertTrue(message.contains("REVIEW_STATUS: ISSUES"));
        assertTrue(message.contains("diff text"));
    }
}
