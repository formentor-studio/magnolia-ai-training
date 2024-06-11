package com.formentor.magnolia.ai.training.infrastructure.openai.dto;

import lombok.Data;

import java.util.List;

/**
 * An object describing an fine-tuned model. Returned by multiple fine-tune requests.
 *
 * https://beta.openai.com/docs/api-reference/fine-tunes
 */
@Data
public class FineTuneResult {
    /**
     * The ID of the fine-tuning job.
     */
    String id;

    /**
     * The type of object returned, should be "fine-tune".
     */
    String object;

    /**
     * The name of the base model.
     */
    String model;

    /**
     * The creation time in epoch seconds.
     */
    Long created_at;

    /**
     * The finished time in epoch seconds.
     */
    Long finished_at;

    /**
     * List of events in this job's lifecycle. Null when getting a list of fine-tune jobs.
     */
    List<FineTuneEvent> events;

    /**
     * The ID of the fine-tuned model, null if tuning job is not finished.
     * This is the id used to call the model.
     */
    String fine_tuned_model;

    /**
     * The specified hyper-parameters for the tuning job.
     */
    HyperParameters hyperparams;

    /**
     * The ID of the organization this model belongs to.
     */
    String organization_id;

    /**
     * Result files for this fine-tune job.
     */
    List<File> result_files;

    /**
     * The status os the fine-tune job. "pending", "succeeded", or "cancelled"
     */
    String status;

    /**
     * Training files for this fine-tune job.
     */
    List<File> training_files;

    /**
     * The last update time in epoch seconds.
     */
    Long updated_at;

    /**
     * Validation files for this fine-tune job.
     */
    List<File> validation_files;
}
