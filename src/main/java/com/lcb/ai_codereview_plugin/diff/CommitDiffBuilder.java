package com.lcb.ai_codereview_plugin.diff;

import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ContentRevision;

import java.util.Collection;

public final class CommitDiffBuilder {

    public static final int DEFAULT_MAX_CHARS = 60_000;

    private final UnifiedDiffFormatter formatter;
    private final int maxChars;

    public CommitDiffBuilder() {
        this(new UnifiedDiffFormatter(), DEFAULT_MAX_CHARS);
    }

    CommitDiffBuilder(UnifiedDiffFormatter formatter, int maxChars) {
        this.formatter = formatter;
        this.maxChars = maxChars;
    }

    public String build(Collection<Change> changes) {
        StringBuilder builder = new StringBuilder();
        for (Change change : changes) {
            appendChange(builder, change);
            if (DiffTextLimiter.truncateIfNeeded(builder, maxChars)) {
                break;
            }
        }
        return builder.toString();
    }

    private void appendChange(StringBuilder builder, Change change) {
        ContentRevision beforeRevision = change.getBeforeRevision();
        ContentRevision afterRevision = change.getAfterRevision();
        String path = resolvePath(beforeRevision, afterRevision);
        try {
            String before = getContent(beforeRevision);
            String after = getContent(afterRevision);
            if (isBinary(before) || isBinary(after)) {
                builder.append("Skipped binary file: ").append(path).append('\n');
                return;
            }
            builder.append(formatter.format(new DiffEntry(path, before, after))).append('\n');
        } catch (VcsException e) {
            builder.append("Skipped unreadable file: ")
                    .append(path)
                    .append(" (")
                    .append(e.getMessage())
                    .append(")\n");
        }
    }

    private static String resolvePath(ContentRevision beforeRevision, ContentRevision afterRevision) {
        ContentRevision revision = afterRevision != null ? afterRevision : beforeRevision;
        if (revision == null || revision.getFile() == null) {
            return "unknown";
        }
        return revision.getFile().getPath();
    }

    private static String getContent(ContentRevision revision) throws VcsException {
        return revision == null ? null : revision.getContent();
    }

    private static boolean isBinary(String content) {
        return content != null && content.indexOf('\0') >= 0;
    }
}
