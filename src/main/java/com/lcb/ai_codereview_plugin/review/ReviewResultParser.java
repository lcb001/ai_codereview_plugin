package com.lcb.ai_codereview_plugin.review;

import java.util.Locale;

public final class ReviewResultParser {

    private static final String PASS_MARKER = "REVIEW_STATUS: PASS";
    private static final String ISSUES_MARKER = "REVIEW_STATUS: ISSUES";

    public ReviewResult parse(String content) throws AiCodeReviewException {
        String normalizedContent = content == null ? "" : content.trim();
        if (normalizedContent.isEmpty()) {
            throw new AiCodeReviewException("AI response is empty.");
        }

        String firstLine = firstNonBlankLine(normalizedContent).toUpperCase(Locale.ROOT);
        if (firstLine.contains(PASS_MARKER)) {
            return new ReviewResult(ReviewStatus.PASS, normalizedContent);
        }
        if (firstLine.contains(ISSUES_MARKER)) {
            return new ReviewResult(ReviewStatus.ISSUES, normalizedContent);
        }

        throw new AiCodeReviewException("AI response does not start with REVIEW_STATUS: PASS or REVIEW_STATUS: ISSUES.");
    }

    private static String firstNonBlankLine(String content) {
        String[] lines = content.split("\\R");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return "";
    }
}
