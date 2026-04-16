package com.lcb.ai_codereview_plugin.diff;

final class DiffTextLimiter {

    private DiffTextLimiter() {
    }

    static boolean truncateIfNeeded(StringBuilder builder, int maxChars) {
        if (builder.length() <= maxChars) {
            return false;
        }
        builder.setLength(maxChars);
        builder.append("\n\n[Diff truncated at ").append(maxChars).append(" characters.]\n");
        return true;
    }
}
