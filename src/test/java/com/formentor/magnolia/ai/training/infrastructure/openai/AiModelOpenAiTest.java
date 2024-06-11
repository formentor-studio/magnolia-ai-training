package com.formentor.magnolia.ai.training.infrastructure.openai;

import info.magnolia.cms.core.FileSystemHelper;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import com.formentor.magnolia.ai.training.AiTrainingModule;
import com.formentor.magnolia.ai.training.domain.Dataset;
import com.formentor.magnolia.ai.training.domain.DatasetMother;
import com.formentor.magnolia.ai.training.domain.Example;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AiModelOpenAiTest {

    @Test
    void train() throws IOException, InterruptedException {
        Dataset dataset = DatasetMother.random();

        MockWebServer mockOpenAIServer = mockupWebServer();
        final String openAIHost = mockOpenAIServer.url(Strings.EMPTY).toString();
        final AiTrainingModule aiTrainingModule = new AiTrainingModule();
        aiTrainingModule.setHost(openAIHost);

        AiModelOpenAI aiModel = new AiModelOpenAI(aiTrainingModule, mock(TokenProvider.class), mockFileSystemHelper());
        aiModel.train("model-name", dataset);

        RecordedRequest requestUploadFile = mockOpenAIServer.takeRequest();
        assertTrue(requestUploadFile.getPath().endsWith("/v1/files"));
        RecordedRequest requestFineTune = mockOpenAIServer.takeRequest();
        assertTrue(requestFineTune.getPath().endsWith("/v1/fine-tunes"));

        mockOpenAIServer.shutdown();
    }

    @Test
    void prepareDataset() throws IOException, ExecutionException, InterruptedException {
        Dataset dataset = DatasetMother.fromExamples(Arrays.asList(
                Example.builder()
                        .prompt("name is Vietnam: Tradition and Today. location is Ho Chi Minh City, Vietnam. duration is 14.")
                        .completion("Vietnam is one of the world's most exotic and culturally rich destinations. A gem among gems, it offers dazzling diversity for visitors.")
                        .build(),
                Example.builder()
                        .prompt("name is Hut to Hut in the Swiss Alps. location is Zurich, Switzerland. duration is 7.")
                        .completion("Some Swiss people are going to be pretty annoyed with us for revealing their secrets. Hush..don&rsquo;t tell anyone, but there&rsquo;s more to Switzerland than skiing! And we don&rsquo;t mean shopping for watches and staying in fancy hotels.")
                        .build()
                ));
        AiModelOpenAI aiModel = new AiModelOpenAI(mock(AiTrainingModule.class), mock(TokenProvider.class), mockFileSystemHelper());

        File file = aiModel.prepareDataset(dataset).get();

        /* The "expected" value has been validated with openai tools
        $ openai tools fine_tunes.prepare_data -f <LOCAL_FILE>
         */
        String expected = "{\"prompt\":\"name is Vietnam: Tradition and Today. location is Ho Chi Minh City, Vietnam. duration is 14.->\", \"completion\": \" Vietnam is one of the world's most exotic and culturally rich destinations. A gem among gems, it offers dazzling diversity for visitors. END\"}\n" +
                "{\"prompt\":\"name is Hut to Hut in the Swiss Alps. location is Zurich, Switzerland. duration is 7.->\", \"completion\": \" Some Swiss people are going to be pretty annoyed with us for revealing their secrets. Hush..don&rsquo;t tell anyone, but there&rsquo;s more to Switzerland than skiing! And we don&rsquo;t mean shopping for watches and staying in fancy hotels. END\"}\n";
        assertEquals(expected, FileUtils.readFileToString(file, "utf-8"));

        file.deleteOnExit();
    }

    private static MockWebServer mockupWebServer() {
        MockWebServer server = new MockWebServer();

        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch (RecordedRequest request) {
                    if (request.getPath().endsWith("/v1/files")) {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setHeader("Content-Type", "application/json")
                                .setBody("{\n" +
                                "  \"object\": \"file\",\n" +
                                "  \"id\": \"file-J5tJNfhKy4uDLQLBud5CagLe\",\n" +
                                "  \"purpose\": \"fine-tune\",\n" +
                                "  \"filename\": \"file.jsonl\",\n" +
                                "  \"bytes\": 632,\n" +
                                "  \"created_at\": 1675189990,\n" +
                                "  \"status\": \"uploaded\",\n" +
                                "  \"status_details\": null\n" +
                                "}");
                    }
                if (request.getPath().endsWith("/v1/fine-tunes")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setHeader("Content-Type", "application/json")
                            .setBody("{\n" +
                            "    \"object\": \"fine-tune\",\n" +
                            "    \"id\": \"ft-NMMkuVHqQCywSuBBfpDmQBjN\",\n" +
                            "    \"hyperparams\": {\n" +
                            "        \"n_epochs\": 4,\n" +
                            "        \"batch_size\": null,\n" +
                            "        \"prompt_loss_weight\": 0.01,\n" +
                            "        \"learning_rate_multiplier\": null\n" +
                            "    },\n" +
                            "    \"organization_id\": \"org-xhj0kKl6Uf8BXqrMGUfRgVhk\",\n" +
                            "    \"model\": \"ada\",\n" +
                            "    \"training_files\": [\n" +
                            "        {\n" +
                            "            \"object\": \"file\",\n" +
                            "            \"id\": \"file-J5tJNfhKy4uDLQLBud5CagLe\",\n" +
                            "            \"purpose\": \"fine-tune\",\n" +
                            "            \"filename\": \"file.jsonl\",\n" +
                            "            \"bytes\": 632,\n" +
                            "            \"created_at\": 1675189990,\n" +
                            "            \"status\": \"processed\",\n" +
                            "            \"status_details\": null\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    \"validation_files\": [],\n" +
                            "    \"result_files\": [],\n" +
                            "    \"created_at\": 1677492540,\n" +
                            "    \"updated_at\": 1677492540,\n" +
                            "    \"status\": \"pending\",\n" +
                            "    \"fine_tuned_model\": null,\n" +
                            "    \"events\": [\n" +
                            "        {\n" +
                            "            \"object\": \"fine-tune-event\",\n" +
                            "            \"level\": \"info\",\n" +
                            "            \"message\": \"Created fine-tune: ft-NMMkuVHqQCywSuBBfpDmQBjN\",\n" +
                            "            \"created_at\": 1677492540\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}");
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);

        return server;
    }

    private FileSystemHelper mockFileSystemHelper() throws IOException {
        FileSystemHelper fileSystemHelper = mock(FileSystemHelper.class);
        String pathname = String.format("src/test/resources/%s", UUID.randomUUID() + ".jsonl");
        when(fileSystemHelper.getTempDirectory()).thenReturn(new File("src/test/resources/"));

        return fileSystemHelper;
    }
}
