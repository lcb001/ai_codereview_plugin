package com.lcb.ai_codereview_plugin.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import java.util.Objects;

public final class AiCodeReviewConfigurable implements Configurable {

    private AiCodeReviewSettingsComponent component;

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "AI Code Review";
    }

    @Override
    public @Nullable JComponent createComponent() {
        component = new AiCodeReviewSettingsComponent();
        return component.getPanel();
    }

    @Override
    public boolean isModified() {
        if (component == null) {
            return false;
        }
        AiCodeReviewSettings settings = AiCodeReviewSettings.getInstance();
        return component.isEnabled() != settings.isEnabled()
                || !sameTrimmed(component.getApiUrl(), settings.getApiUrl())
                || !sameTrimmed(component.getModel(), settings.getModel())
                || !Objects.equals(component.getPrompt(), settings.getCustomPrompt())
                || !sameTrimmed(component.getApiKey(), AiCodeReviewApiKeyStore.getApiKey());
    }

    @Override
    public void apply() {
        if (component == null) {
            return;
        }
        AiCodeReviewSettings.getInstance().update(
                component.isEnabled(),
                component.getApiUrl(),
                component.getModel(),
                component.getPrompt()
        );
        AiCodeReviewApiKeyStore.setApiKey(component.getApiKey());
    }

    @Override
    public void reset() {
        if (component == null) {
            return;
        }
        AiCodeReviewSettings settings = AiCodeReviewSettings.getInstance();
        component.setEnabled(settings.isEnabled());
        component.setApiUrl(settings.getApiUrl());
        component.setModel(settings.getModel());
        component.setPrompt(settings.getCustomPrompt());
        component.setApiKey(AiCodeReviewApiKeyStore.getApiKey());
    }

    @Override
    public void disposeUIResources() {
        component = null;
    }

    private static boolean sameTrimmed(String left, String right) {
        return normalize(left).equals(normalize(right));
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
