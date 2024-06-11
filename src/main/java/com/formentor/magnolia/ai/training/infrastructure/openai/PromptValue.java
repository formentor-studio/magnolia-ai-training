package com.formentor.magnolia.ai.training.infrastructure.openai;

public class PromptValue {
    public static final String PROMPT_SEPARATOR = "->";

    private final String value;

    public PromptValue(String value) {
        this.value = value;
    }

    public static PromptValue fromString(String value) {
        return new PromptValue(value);
    }

    public String toString() {
        return String.format("%s%s", escape(value), PROMPT_SEPARATOR);
    }

    // TODO. This code is shared with CompletionValue. DO NOT REPEAT
    /**
     * Escape protected chars - e.g. 'The "house"' with 'The \"house\"' -
     * @param raw
     * @return
     */
    public String escape(String raw) {
        return raw.replaceAll("\"", "\\\\\"");
    }
}
