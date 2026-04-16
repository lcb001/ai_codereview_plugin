package com.lcb.ai_codereview_plugin.review;

public final class ReviewPromptBuilder {

    private static final String DEFAULT_PROMPT = """
            你是一名严谨的资深代码审查工程师。请审查本次 commit 的 diff，重点关注：
            1. 可能导致 bug 的逻辑错误、空指针、边界条件、并发问题。
            2. 安全风险，例如敏感信息泄露、注入、权限绕过。
            3. 性能问题和资源泄漏。
            4. 可维护性问题，以及明显遗漏的测试。

            如果没有发现明确问题，请简洁说明通过。不要因为风格偏好给出阻塞意见。
            """;

    private static final String OUTPUT_PROTOCOL = """

            输出要求：
            第一行必须且只能是 REVIEW_STATUS: PASS 或 REVIEW_STATUS: ISSUES。
            如果是 REVIEW_STATUS: ISSUES，请按问题列出文件、风险原因和建议修改方式。
            后续内容请使用中文。
            """;

    public String buildUserMessage(String customPrompt, String diffText) {
        String reviewPrompt = customPrompt == null || customPrompt.trim().isEmpty()
                ? DEFAULT_PROMPT
                : customPrompt.trim();
        return reviewPrompt
                + OUTPUT_PROTOCOL
                + "\n\n本次提交 diff：\n"
                + diffText;
    }
}
