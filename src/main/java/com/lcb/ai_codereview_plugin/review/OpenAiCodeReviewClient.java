package com.lcb.ai_codereview_plugin.review;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.progress.ProgressManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public final class OpenAiCodeReviewClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(60);

    private final HttpClient httpClient;
    private final OpenAiChatRequestBuilder requestBuilder;
    private final ReviewResultParser resultParser;

    public OpenAiCodeReviewClient() {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build(),
                new OpenAiChatRequestBuilder(),
                new ReviewResultParser());
    }

    OpenAiCodeReviewClient(HttpClient httpClient,
                           OpenAiChatRequestBuilder requestBuilder,
                           ReviewResultParser resultParser) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.resultParser = resultParser;
    }

    public ReviewResult review(AiCodeReviewConfig config, String userMessage) throws AiCodeReviewException {
        ProgressManager.checkCanceled();
        String body = requestBuilder.build(config.getModel(), userMessage);
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(URI.create(config.getApiUrl()))
                    .timeout(TIMEOUT)
                    .header("Authorization", "Bearer " + config.getApiKey())
                    .header("Content-Type", "application/json; charset=utf-8")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new AiCodeReviewException("API URL is invalid: " + config.getApiUrl(), e);
        }

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new AiCodeReviewException("Failed to call AI API: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AiCodeReviewException("AI review request was interrupted.", e);
        }

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new AiCodeReviewException("AI API returned HTTP " + response.statusCode() + ": " + response.body());
        }

        return resultParser.parse(extractMessageContent(response.body()));
    }

    private static String extractMessageContent(String responseBody) throws AiCodeReviewException {
        try {
            JsonObject root = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray choices = root.getAsJsonArray("choices");
            if (choices == null || choices.isEmpty()) {
                throw new AiCodeReviewException("AI response does not contain choices.");
            }
            JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
            if (message == null || !message.has("content")) {
                throw new AiCodeReviewException("AI response does not contain choices[0].message.content.");
            }
            return message.get("content").getAsString();
        } catch (IllegalStateException | NullPointerException e) {
            throw new AiCodeReviewException("Failed to parse AI response JSON.", e);
        }
    }
}
