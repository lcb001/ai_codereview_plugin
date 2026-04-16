package com.lcb.ai_codereview_plugin.review;

public final class AiCodeReviewException extends Exception {

    public AiCodeReviewException(String message) {
        super(message);
    }

    public AiCodeReviewException(String message, Throwable cause) {
        super(message, cause);
    }
}
