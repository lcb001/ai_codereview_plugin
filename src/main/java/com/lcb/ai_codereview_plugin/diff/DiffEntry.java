package com.lcb.ai_codereview_plugin.diff;

public final class DiffEntry {

    private final String path;
    private final String beforeContent;
    private final String afterContent;

    public DiffEntry(String path, String beforeContent, String afterContent) {
        this.path = path == null ? "unknown" : path;
        this.beforeContent = beforeContent;
        this.afterContent = afterContent;
    }

    public String getPath() {
        return path;
    }

    public String getBeforeContent() {
        return beforeContent;
    }

    public String getAfterContent() {
        return afterContent;
    }

    public boolean isAdded() {
        return beforeContent == null;
    }

    public boolean isDeleted() {
        return afterContent == null;
    }
}
