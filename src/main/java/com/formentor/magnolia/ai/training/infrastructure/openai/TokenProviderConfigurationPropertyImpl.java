package com.formentor.magnolia.ai.training.infrastructure.openai;

import info.magnolia.init.MagnoliaConfigurationProperties;

import javax.inject.Inject;

/**
 * Provider for the OpenAI token.
 *
 * This implementation fetches the token from environment, in case you are using Magnolia Passwords, inject the required implementation.
 */
public class TokenProviderConfigurationPropertyImpl implements TokenProvider {
    private final String token;

    @Inject
    public TokenProviderConfigurationPropertyImpl(MagnoliaConfigurationProperties configurationProperties) {
        token = configurationProperties.getProperty("OPENAI_TOKEN");
    }

    @Override
    public String get() {
        return token;
    }
}
