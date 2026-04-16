package com.lcb.ai_codereview_plugin.diff;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class UnifiedDiffFormatterTest {

    @Test
    public void formatsChangedFileAsUnifiedDiff() {
        String diff = new UnifiedDiffFormatter().format(new DiffEntry("src/App.java", "a\nold\nc\n", "a\nnew\nc\n"));

        assertTrue(diff.contains("diff --git a/src/App.java b/src/App.java"));
        assertTrue(diff.contains("-old"));
        assertTrue(diff.contains("+new"));
    }

    @Test
    public void formatsAddedFileWithDevNullBeforePath() {
        String diff = new UnifiedDiffFormatter().format(new DiffEntry("README.md", null, "hello\n"));

        assertTrue(diff.contains("--- /dev/null"));
        assertTrue(diff.contains("+++ b/README.md"));
        assertTrue(diff.contains("+hello"));
    }
}
