package com.lcb.ai_codereview_plugin.settings;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;

public final class AiCodeReviewApiKeyStore {

    private static final String SERVICE_NAME = "com.lcb.ai_codereview_plugin.apiKey";
    private static final String USER_NAME = "apiKey";

    private AiCodeReviewApiKeyStore() {
    }

    public static String getApiKey() {
        Credentials credentials = PasswordSafe.getInstance().get(createCredentialAttributes());
        return credentials == null ? "" : emptyIfNull(credentials.getPasswordAsString());
    }

    public static void setApiKey(String apiKey) {
        String normalized = apiKey == null ? "" : apiKey.trim();
        Credentials credentials = normalized.isEmpty() ? null : new Credentials(USER_NAME, normalized);
        PasswordSafe.getInstance().set(createCredentialAttributes(), credentials);
    }

    private static CredentialAttributes createCredentialAttributes() {
        return new CredentialAttributes(SERVICE_NAME, USER_NAME);
    }

    private static String emptyIfNull(String value) {
        return value == null ? "" : value;
    }
}
