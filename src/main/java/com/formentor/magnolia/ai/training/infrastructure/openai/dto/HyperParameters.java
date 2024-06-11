package com.formentor.magnolia.ai.training.infrastructure.openai.dto;

import lombok.Data;

/**
 * Fine-tuning job hyperparameters
 *
 * https://beta.openai.com/docs/api-reference/fine-tunes
 */
@Data
public class HyperParameters {

    /**
     * The batch size to use for training.
     */
    String batch_size;

    /**
     * The value indicating whether to compute classification metrics
     */
    Boolean compute_classification_metrics;

    /**
     * The learning rate multiplier to use for training.
     */
    Double learning_rate_multiplier;

    /**
     * The number of epochs to train the model for.
     */
    Integer n_epochs;

    /**
     * The weight to use for loss on the prompt tokens.
     */
    Double prompt_loss_weight;
}
