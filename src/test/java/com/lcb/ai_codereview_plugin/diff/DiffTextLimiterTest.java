package com.lcb.ai_codereview_plugin.diff;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DiffTextLimiterTest {

    @Test
    public void truncatesLargeDiffs() {
        StringBuilder builder = new StringBuilder("x".repeat(200));

        boolean truncated = DiffTextLimiter.truncateIfNeeded(builder, 80);

        assertTrue(truncated);
        assertTrue(builder.toString().contains("[Diff truncated at 80 characters.]"));
    }
}
