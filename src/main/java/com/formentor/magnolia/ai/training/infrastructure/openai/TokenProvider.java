package com.formentor.magnolia.ai.training.infrastructure.openai;

import java.util.function.Supplier;

/**
 * Provider for the OpenAI token.
 *
 * This implementation fetches the token from environment, in case you are using Magnolia Passwords, inject the required implementation.
 */
public interface TokenProvider extends Supplier<String> {

    /*
    private final String token;

    @Inject
    public TokenProvider(MagnoliaConfigurationProperties configurationProperties) {
        token = configurationProperties.getProperty("OPENAI_TOKEN");
    }

    @Override
    public String get() {
        return token;
    }
     */
}
