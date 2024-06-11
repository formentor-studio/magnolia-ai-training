package com.formentor.magnolia.ai.training.infrastructure.azure;

import com.formentor.magnolia.ai.training.infrastructure.openai.dto.File;
import com.formentor.magnolia.ai.training.infrastructure.openai.dto.FineTuneRequest;
import com.formentor.magnolia.ai.training.infrastructure.openai.dto.FineTuneResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AzureOpenAiApiTest {
    @Disabled // Integration test
    @Test
    void uploadFile() throws IOException {
        final String FILE_PURPOSE_FINE_TUNING = "fine-tune";

        String host = "https://aoaipocmhi-mgnl.openai.azure.com/openai";
        String apiKey = System.getenv("AZURE_API_KEY");
        String apiVersion = "2022-12-01";

        final AzureOpenAiApi api = new AzureOpenAiApi(host, apiKey, apiVersion);
        File file = api.uploadFile(FILE_PURPOSE_FINE_TUNING, new java.io.File("src/test/resources/fine-tune-example.jsonl")).join();

        assertNotNull(file);
    }

    @Disabled // Integration test
    @Test
    void createFineTune() throws IOException, ExecutionException, InterruptedException {
        String host = "https://aoaipocmhi-mgnl.openai.azure.com/openai";
        String apiKey = System.getenv("AZURE_API_KEY");
        String apiVersion = "2022-12-01";
        FineTuneRequest fineTuneRequest = FineTuneRequest.builder()
                .training_file("file-51...6d")
                .model("ada")
                .suffix("magnolia")
                .build();

        final AzureOpenAiApi api = new AzureOpenAiApi(host, apiKey, apiVersion);
        FineTuneResult fineTuneResult = api.createFineTune(fineTuneRequest).get();

        assertNotNull(fineTuneResult);

    }
}
