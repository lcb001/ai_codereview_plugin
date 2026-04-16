package com.lcb.ai_codereview_plugin.review;

public final class ReviewResult {

    private final ReviewStatus status;
    private final String content;

    public ReviewResult(ReviewStatus status, String content) {
        this.status = status;
        this.content = content == null ? "" : content;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    public boolean hasIssues() {
        return status == ReviewStatus.ISSUES;
    }
}
