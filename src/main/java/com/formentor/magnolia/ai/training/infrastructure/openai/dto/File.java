package com.formentor.magnolia.ai.training.infrastructure.openai.dto;

import lombok.Data;

/**
 * A file uploaded to OpenAi
 *
 * https://beta.openai.com/docs/api-reference/files
 */
@Data
public class File {

    /**
     * The unique id of this file.
     */
    String id;

    /**
     * The type of object returned, should be "file".
     */
    String object;

    /**
     * File size in bytes.
     */
    Long bytes;

    /**
     * The creation time in epoch seconds.
     */
    Long created_at;

    /**
     * The update time in epoch seconds.
     */
    Long updated_at;

    /**
     * The name of the file.
     */
    String filename;

    /**
     * Description of the file's purpose.
     */
    String purpose;

    /**
     * Status
     */
    String status;

    /**
     * Status details
     */
    String status_details;
}
