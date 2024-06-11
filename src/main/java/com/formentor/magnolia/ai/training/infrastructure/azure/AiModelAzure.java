package com.formentor.magnolia.ai.training.infrastructure.azure;

import com.formentor.magnolia.ai.training.AiTrainingModule;
import com.formentor.magnolia.ai.training.domain.AiModel;
import com.formentor.magnolia.ai.training.domain.Dataset;
import com.formentor.magnolia.ai.training.infrastructure.openai.ExampleValue;
import com.formentor.magnolia.ai.training.infrastructure.openai.dto.FineTuneRequest;
import com.formentor.magnolia.ai.training.infrastructure.openai.dto.FineTuneResult;
import com.machinezoo.noexception.Exceptions;
import info.magnolia.cms.core.FileSystemHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class AiModelAzure implements AiModel {
    private final static String FILE_PURPOSE_FINE_TUNING = "fine-tune";

    private final AiTrainingModule.Azure azureDefinition;
    private final AzureOpenAiApi api;
    private final FileSystemHelper fileSystemHelper;

    @Inject
    public AiModelAzure(AiTrainingModule aiTrainingModule, AzureApiKeyProvider apiTokenProvider, FileSystemHelper fileSystemHelper) {
        this.azureDefinition = aiTrainingModule.getAzure();
        this.fileSystemHelper = fileSystemHelper;
        this.api = (azureDefinition == null)? null: new AzureOpenAiApi(azureDefinition.getHost(), apiTokenProvider.get(), azureDefinition.getApiVersion());
    }

    @Override
    public CompletableFuture<String> train(String modelName, Dataset dataset) {
        try {
            FineTuneResult fineTuneResult = prepareDataset(dataset) // 1. PREPARE DATASET
                    .thenCompose(file -> {                          // 2. UPLOAD FILE
                        try {
                             return api.uploadFile(FILE_PURPOSE_FINE_TUNING, file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }})
                    .thenApply(training_file -> {
                        Exceptions.sneak().run(() -> Thread.sleep(3_000)); // Wait to be sure that importing is completed in Azure
                        return training_file;
                    })
                    .thenCompose(training_file -> {                  // 3. CREATE FINE TUNE
                        try {
                            return api.createFineTune(FineTuneRequest.builder()
                                    .training_file(training_file.getId())
                                    .model(azureDefinition.getBaseModel())
                                    .suffix(modelName)
                                    .build());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }})
                    .get();
            log.warn("OpenAI fine-tune job \"{}\" launched \"{}\"", fineTuneResult.getId(), fineTuneResult.getStatus());
            return CompletableFuture.completedFuture(fineTuneResult.getId());
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prepares dataset to train an OpenAI model
     * See:
     * - https://platform.openai.com/docs/guides/fine-tuning/preparing-your-dataset
     * - https://platform.openai.com/docs/guides/fine-tuning/case-study-product-description-based-on-a-technical-list-of-properties
     * @param dataset
     * @return
     */
    // TODO Remember to make this method "private", it is just "protected" for testing purposes.
    protected CompletableFuture<File> prepareDataset(Dataset dataset) throws IOException {
        String jsonl = dataset.getExamples().stream().reduce("", (acc, example) ->
            acc.concat(
                    new ExampleValue(example.getPrompt(), example.getCompletion()).jsonl()
            )
            , String::concat);

        File localFile = createTempFile(".jsonl");
        FileUtils.writeStringToFile(localFile, jsonl, "utf-8");

        return CompletableFuture.completedFuture(localFile);
    }


    private File createTempFile(String extension) throws IOException {
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), extension, fileSystemHelper.getTempDirectory());
        tempFile.deleteOnExit();
        return tempFile;
    }
}
