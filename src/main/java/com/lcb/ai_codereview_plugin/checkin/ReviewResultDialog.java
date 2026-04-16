package com.lcb.ai_codereview_plugin.checkin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

final class ReviewResultDialog extends DialogWrapper {

    private final String reviewContent;

    ReviewResultDialog(@Nullable Project project, String reviewContent) {
        super(project, true);
        this.reviewContent = reviewContent == null ? "" : reviewContent;
        setTitle("AI Code Review Issues");
        setOKButtonText("Continue Commit");
        setCancelButtonText("Cancel Commit");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JBTextArea textArea = new JBTextArea(reviewContent, 24, 100);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return new JBScrollPane(textArea);
    }
}
