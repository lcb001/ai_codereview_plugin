# AI Code Review

AI Code Review is an IntelliJ IDEA plugin that runs an OpenAI-compatible code review before commit. It reads the selected VCS changes, builds a unified diff, sends the diff to your configured AI endpoint, and shows review findings before the commit continues.

中文：AI Code Review 是一个 IntelliJ IDEA 插件，用于在提交代码前调用兼容 OpenAI 的模型进行代码审查。插件会读取当前选中的 VCS 变更，生成 unified diff，并在提交继续前展示 AI 审查结果。

## Features

- Runs automatically in the IntelliJ IDEA commit workflow when enabled.
- Reviews only the selected VCS changes before commit.
- Supports OpenAI-compatible Chat Completions APIs.
- Configurable API URL, API key, model name, enable switch, and custom review prompt.
- Stores the API key in the IDE PasswordSafe instead of plain settings files.
- Focuses on concrete bugs, security risks, performance issues, resource leaks, maintainability problems, and missing tests.
- Lets users continue or cancel the commit when review issues are found.

## Compatibility

- IntelliJ IDEA: 2023.1 to 2024.1.x
- IntelliJ Platform build range: `231` to `241.*`
- Java for building: JDK 17
- Gradle wrapper: included in this repository

## Configuration

Open `Settings | Tools | AI Code Review` and configure:

- `Enable AI Code Review before commit`
- `API URL`, for example `https://api.openai.com/v1/chat/completions`
- `API Key`
- `Model`, for example an OpenAI-compatible chat model
- `Custom Review Prompt`, optional

If the custom prompt is empty, the plugin uses the built-in Chinese review prompt.

## Usage

1. Enable the plugin in `Settings | Tools | AI Code Review`.
2. Configure the API URL, API key, and model.
3. Open the commit window and select the changes you want to commit.
4. Commit as usual.
5. If the AI review reports issues, inspect the result dialog and choose whether to continue or cancel the commit.

## Security Notes

- API keys are stored through IntelliJ Platform PasswordSafe.
- Do not commit local environment files, signing certificates, private keys, or Marketplace tokens.
- Review AI output before relying on it for commit decisions.

## License

No license has been specified yet. Add a `LICENSE` file before making the repository public if you want others to use, modify, or redistribute the plugin.
