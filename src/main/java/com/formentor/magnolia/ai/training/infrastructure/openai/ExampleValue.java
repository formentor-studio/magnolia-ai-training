package com.formentor.magnolia.ai.training.infrastructure.openai;

public class ExampleValue {
    private final PromptValue prompt;
    private final CompletionValue completion;

    public ExampleValue(String prompt, String completion) {
        this.prompt = PromptValue.fromString(prompt);
        this.completion = CompletionValue.fromString(completion);
    }

    public String jsonl() {
        // https://platform.openai.com/docs/guides/fine-tuning/preparing-your-dataset
        // $ openai tools fine_tunes.prepare_data -f  src/test/resources/fine-tune-example.jsonl
        return String.format("{\"prompt\":\"%s\", \"completion\": \" %s\"}\n", prompt, completion);
    }


}
