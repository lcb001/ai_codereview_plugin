package com.lcb.ai_codereview_plugin.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

@State(name = "AiCodeReviewSettings", storages = @Storage("aiCodeReviewSettings.xml"))
public final class AiCodeReviewSettings implements PersistentStateComponent<AiCodeReviewSettings.State> {

    private State state = new State();

    public static AiCodeReviewSettings getInstance() {
        return ApplicationManager.getApplication().getService(AiCodeReviewSettings.class);
    }

    @Override
    public @NotNull State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
    }

    public boolean isEnabled() {
        return state.enabled;
    }

    public String getApiUrl() {
        return state.apiUrl;
    }

    public String getModel() {
        return state.model;
    }

    public String getCustomPrompt() {
        return state.customPrompt;
    }

    public void update(boolean enabled, String apiUrl, String model, String customPrompt) {
        state.enabled = enabled;
        state.apiUrl = normalize(apiUrl);
        state.model = normalize(model);
        state.customPrompt = customPrompt == null ? "" : customPrompt;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public static final class State {
        public boolean enabled = true;
        public String apiUrl = "";
        public String model = "";
        public String customPrompt = "";
    }
}
