package com.formentor.magnolia.ai.training.infrastructure.azure;

import info.magnolia.init.MagnoliaConfigurationProperties;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provider for the OpenAI token.
 *
 * This implementation fetches the api-key from environment variable, in case you are using Magnolia Passwords, inject the required implementation.
 */
@Singleton
public class AzureApiKeyProviderFromConfigurationProperty implements AzureApiKeyProvider {

    private final String token;

    @Inject
    public AzureApiKeyProviderFromConfigurationProperty(MagnoliaConfigurationProperties configurationProperties) {
        token = configurationProperties.getProperty("AZURE_API_KEY");
    }

    @Override
    public String get() {
        return token;
    }
}
