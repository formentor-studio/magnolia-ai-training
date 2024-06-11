package com.formentor.magnolia.ai.training.infrastructure.openai;

public class CompletionValue {
    public static final String COMPLETION_ENDING = " END";

    private final String value;

    public CompletionValue(String value) {
        this.value = value;
    }

    public static CompletionValue fromString(String value) {
        return new CompletionValue(value);
    }

    public String toString() {
        return String.format("%s%s", escape(value), COMPLETION_ENDING);
    }

    // TODO. This code is shared with PromptValue. DO NOT REPEAT
    /**
     * Escape protected chars - e.g. 'The "house"' with 'The \"house\"' -
     * @param raw
     * @return
     */
    public String escape(String raw) {
        return raw.replaceAll("\"", "\\\\\"");
    }
}
