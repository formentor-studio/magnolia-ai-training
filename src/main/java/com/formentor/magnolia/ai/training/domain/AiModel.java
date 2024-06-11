package com.formentor.magnolia.ai.training.domain;

import java.util.concurrent.CompletableFuture;

/**
 * Domain class to fine tune LLM models
 */

public interface AiModel {
    CompletableFuture<String> train(String modelName, Dataset dataset);
    default CompletableFuture<String> predict(String modelName, Object prompt) {
        throw new RuntimeException("Unsupported operation");
    }
}
