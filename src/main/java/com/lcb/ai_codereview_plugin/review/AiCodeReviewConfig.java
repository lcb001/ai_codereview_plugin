package com.lcb.ai_codereview_plugin.review;

public final class AiCodeReviewConfig {

    private final String apiUrl;
    private final String apiKey;
    private final String model;
    private final String customPrompt;

    public AiCodeReviewConfig(String apiUrl, String apiKey, String model, String customPrompt) {
        this.apiUrl = normalize(apiUrl);
        this.apiKey = normalize(apiKey);
        this.model = normalize(model);
        this.customPrompt = customPrompt == null ? "" : customPrompt;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getModel() {
        return model;
    }

    public String getCustomPrompt() {
        return customPrompt;
    }

    public String validate() {
        if (apiUrl.isEmpty()) {
            return "API URL is required.";
        }
        if (apiKey.isEmpty()) {
            return "API Key is required.";
        }
        if (model.isEmpty()) {
            return "Model is required.";
        }
        return "";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
