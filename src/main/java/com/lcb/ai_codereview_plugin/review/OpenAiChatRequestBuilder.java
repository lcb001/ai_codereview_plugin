package com.lcb.ai_codereview_plugin.review;

import com.google.gson.Gson;

import java.util.List;

public final class OpenAiChatRequestBuilder {

    private static final Gson GSON = new Gson();

    public String build(String model, String userMessage) {
        ChatCompletionRequest request = new ChatCompletionRequest(
                model,
                List.of(
                        new Message("system", "You are an expert code review assistant."),
                        new Message("user", userMessage)
                ),
                0.2
        );
        return GSON.toJson(request);
    }

    private record ChatCompletionRequest(String model, List<Message> messages, double temperature) {
    }

    private record Message(String role, String content) {
    }
}
