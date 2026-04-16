package com.lcb.ai_codereview_plugin.settings;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public final class AiCodeReviewSettingsComponent {

    private final JPanel mainPanel;
    private final JBCheckBox enabledCheckBox = new JBCheckBox("Enable AI Code Review before commit");
    private final JBTextField apiUrlField = new JBTextField();
    private final JBPasswordField apiKeyField = new JBPasswordField();
    private final JBTextField modelField = new JBTextField();
    private final JBTextArea promptTextArea = new JBTextArea(12, 60);
    private final JButton resetPromptButton = new JButton("Reset to Default Prompt");

    public AiCodeReviewSettingsComponent() {
        apiUrlField.getEmptyText().setText("https://api.openai.com/v1/chat/completions");
        modelField.getEmptyText().setText("gpt-4o-mini or another OpenAI-compatible model");
        promptTextArea.setLineWrap(true);
        promptTextArea.setWrapStyleWord(true);
        promptTextArea.getEmptyText().setText("Leave empty to use the default Chinese review prompt.");
        resetPromptButton.addActionListener(event -> promptTextArea.setText(""));

        JPanel promptPanel = new JPanel(new BorderLayout());
        promptPanel.add(new JBScrollPane(promptTextArea), BorderLayout.CENTER);
        promptPanel.add(resetPromptButton, BorderLayout.SOUTH);

        JBLabel apiKeyHint = new JBLabel("Stored in the IDE PasswordSafe, not in the settings XML.");
        apiKeyHint.setForeground(JBColor.GRAY);

        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(enabledCheckBox, 1)
                .addLabeledComponent("API URL", apiUrlField, 1, false)
                .addLabeledComponent("API Key", apiKeyField, 1, false)
                .addComponent(apiKeyHint, 1)
                .addLabeledComponent("Model", modelField, 1, false)
                .addLabeledComponent("Custom Review Prompt", promptPanel, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JComponent getPanel() {
        return mainPanel;
    }

    public boolean isEnabled() {
        return enabledCheckBox.isSelected();
    }

    public void setEnabled(boolean enabled) {
        enabledCheckBox.setSelected(enabled);
    }

    public String getApiUrl() {
        return apiUrlField.getText();
    }

    public void setApiUrl(String apiUrl) {
        apiUrlField.setText(apiUrl);
    }

    public String getApiKey() {
        return new String(apiKeyField.getPassword());
    }

    public void setApiKey(String apiKey) {
        apiKeyField.setText(apiKey);
    }

    public String getModel() {
        return modelField.getText();
    }

    public void setModel(String model) {
        modelField.setText(model);
    }

    public String getPrompt() {
        return promptTextArea.getText();
    }

    public void setPrompt(String prompt) {
        promptTextArea.setText(prompt);
    }
}
