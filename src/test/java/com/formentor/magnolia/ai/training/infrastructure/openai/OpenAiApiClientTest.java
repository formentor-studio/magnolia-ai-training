package com.formentor.magnolia.ai.training.infrastructure.openai;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class OpenAiApiClientTest {

    @Disabled // Integration test
    @Test
    void uploadFile() throws IOException {
        final String FILE_PURPOSE_FINE_TUNING = "fine-tune";

        String host = "https://api.openai.com";
        String token = System.getenv("OPENAI_TOKEN");

        OpenAiApi api = new OpenAiApi(host, token);
        api.uploadFile(FILE_PURPOSE_FINE_TUNING, new java.io.File("src/test/resources/fine-tune-example.jsonl")).join();
    }
}
