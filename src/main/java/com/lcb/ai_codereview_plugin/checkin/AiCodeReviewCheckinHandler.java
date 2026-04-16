package com.lcb.ai_codereview_plugin.checkin;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.util.PairConsumer;
import com.lcb.ai_codereview_plugin.diff.CommitDiffBuilder;
import com.lcb.ai_codereview_plugin.review.AiCodeReviewConfig;
import com.lcb.ai_codereview_plugin.review.AiCodeReviewException;
import com.lcb.ai_codereview_plugin.review.OpenAiCodeReviewClient;
import com.lcb.ai_codereview_plugin.review.ReviewPromptBuilder;
import com.lcb.ai_codereview_plugin.review.ReviewResult;
import com.lcb.ai_codereview_plugin.settings.AiCodeReviewApiKeyStore;
import com.lcb.ai_codereview_plugin.settings.AiCodeReviewSettings;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class AiCodeReviewCheckinHandler extends CheckinHandler {

    private final CheckinProjectPanel panel;
    private final CommitDiffBuilder diffBuilder;
    private final ReviewPromptBuilder promptBuilder;
    private final OpenAiCodeReviewClient client;

    public AiCodeReviewCheckinHandler(CheckinProjectPanel panel) {
        this(panel, new CommitDiffBuilder(), new ReviewPromptBuilder(), new OpenAiCodeReviewClient());
    }

    AiCodeReviewCheckinHandler(CheckinProjectPanel panel,
                               CommitDiffBuilder diffBuilder,
                               ReviewPromptBuilder promptBuilder,
                               OpenAiCodeReviewClient client) {
        this.panel = panel;
        this.diffBuilder = diffBuilder;
        this.promptBuilder = promptBuilder;
        this.client = client;
    }

    @Override
    public ReturnResult beforeCheckin(@Nullable CommitExecutor executor, PairConsumer<Object, Object> additionalDataConsumer) {
        return beforeCheckin();
    }

    @Override
    public ReturnResult beforeCheckin() {
        AiCodeReviewSettings settings = AiCodeReviewSettings.getInstance();
        if (!settings.isEnabled()) {
            return ReturnResult.COMMIT;
        }

        Project project = panel.getProject();
        Collection<Change> changes = panel.getSelectedChanges();
        if (changes.isEmpty()) {
            return ReturnResult.COMMIT;
        }

        AiCodeReviewConfig config = new AiCodeReviewConfig(
                settings.getApiUrl(),
                AiCodeReviewApiKeyStore.getApiKey(),
                settings.getModel(),
                settings.getCustomPrompt()
        );
        String validationError = config.validate();
        if (!validationError.isEmpty()) {
            return askContinueAfterFailure(project, "AI Code Review is not configured: " + validationError);
        }

        try {
            ReviewResult result = ProgressManager.getInstance().runProcessWithProgressSynchronously(
                    () -> review(changes, config),
                    "AI Code Review",
                    true,
                    project
            );
            if (result.hasIssues()) {
                return new ReviewResultDialog(project, result.getContent()).showAndGet()
                        ? ReturnResult.COMMIT
                        : ReturnResult.CANCEL;
            }
            return ReturnResult.COMMIT;
        } catch (ProcessCanceledException e) {
            return ReturnResult.CANCEL;
        } catch (AiCodeReviewException e) {
            return askContinueAfterFailure(project, e.getMessage());
        }
    }

    private ReviewResult review(Collection<Change> changes, AiCodeReviewConfig config) throws AiCodeReviewException {
        String diff = diffBuilder.build(changes);
        if (diff.trim().isEmpty()) {
            throw new AiCodeReviewException("There are no readable text changes to review.");
        }
        String userMessage = promptBuilder.buildUserMessage(config.getCustomPrompt(), diff);
        return client.review(config, userMessage);
    }

    private static ReturnResult askContinueAfterFailure(Project project, String message) {
        int result = Messages.showOkCancelDialog(
                project,
                message + "\n\nContinue commit without AI Code Review?",
                "AI Code Review",
                "Continue Commit",
                "Cancel Commit",
                Messages.getWarningIcon()
        );
        return result == Messages.OK ? ReturnResult.COMMIT : ReturnResult.CANCEL;
    }
}
