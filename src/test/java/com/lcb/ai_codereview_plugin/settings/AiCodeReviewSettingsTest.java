package com.lcb.ai_codereview_plugin.settings;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AiCodeReviewSettingsTest {

    @Test
    public void updatesAndNormalizesPersistentStateFields() {
        AiCodeReviewSettings settings = new AiCodeReviewSettings();

        settings.update(false, " https://example.com/v1/chat/completions ", " model ", "prompt");

        assertFalse(settings.isEnabled());
        assertEquals("https://example.com/v1/chat/completions", settings.getApiUrl());
        assertEquals("model", settings.getModel());
        assertEquals("prompt", settings.getCustomPrompt());
    }
}
