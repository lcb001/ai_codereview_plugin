package com.lcb.ai_codereview_plugin.diff;

import java.util.ArrayList;
import java.util.List;

public final class UnifiedDiffFormatter {

    private static final int CONTEXT_LINES = 3;

    public String format(DiffEntry entry) {
        String path = entry.getPath();
        String before = entry.getBeforeContent();
        String after = entry.getAfterContent();
        StringBuilder builder = new StringBuilder();

        builder.append("diff --git a/").append(path).append(" b/").append(path).append('\n');
        builder.append("--- ").append(entry.isAdded() ? "/dev/null" : "a/" + path).append('\n');
        builder.append("+++ ").append(entry.isDeleted() ? "/dev/null" : "b/" + path).append('\n');
        builder.append("@@\n");

        List<String> beforeLines = splitLines(before == null ? "" : before);
        List<String> afterLines = splitLines(after == null ? "" : after);

        int prefix = commonPrefixLength(beforeLines, afterLines);
        int suffix = commonSuffixLength(beforeLines, afterLines, prefix);

        int beforeChangedEnd = beforeLines.size() - suffix;
        int afterChangedEnd = afterLines.size() - suffix;

        int contextStart = Math.max(0, prefix - CONTEXT_LINES);
        for (int i = contextStart; i < prefix; i++) {
            builder.append(' ').append(beforeLines.get(i)).append('\n');
        }
        for (int i = prefix; i < beforeChangedEnd; i++) {
            builder.append('-').append(beforeLines.get(i)).append('\n');
        }
        for (int i = prefix; i < afterChangedEnd; i++) {
            builder.append('+').append(afterLines.get(i)).append('\n');
        }
        int suffixEnd = Math.min(beforeLines.size(), beforeChangedEnd + CONTEXT_LINES);
        for (int i = beforeChangedEnd; i < suffixEnd; i++) {
            builder.append(' ').append(beforeLines.get(i)).append('\n');
        }

        return builder.toString();
    }

    private static int commonPrefixLength(List<String> beforeLines, List<String> afterLines) {
        int max = Math.min(beforeLines.size(), afterLines.size());
        int index = 0;
        while (index < max && beforeLines.get(index).equals(afterLines.get(index))) {
            index++;
        }
        return index;
    }

    private static int commonSuffixLength(List<String> beforeLines, List<String> afterLines, int prefixLength) {
        int beforeIndex = beforeLines.size() - 1;
        int afterIndex = afterLines.size() - 1;
        int count = 0;
        while (beforeIndex >= prefixLength
                && afterIndex >= prefixLength
                && beforeLines.get(beforeIndex).equals(afterLines.get(afterIndex))) {
            beforeIndex--;
            afterIndex--;
            count++;
        }
        return count;
    }

    private static List<String> splitLines(String value) {
        String normalized = value.replace("\r\n", "\n").replace('\r', '\n');
        String[] rawLines = normalized.split("\n", -1);
        List<String> lines = new ArrayList<>();
        int limit = rawLines.length;
        if (limit > 0 && rawLines[limit - 1].isEmpty()) {
            limit--;
        }
        for (int i = 0; i < limit; i++) {
            lines.add(rawLines[i]);
        }
        return lines;
    }
}
