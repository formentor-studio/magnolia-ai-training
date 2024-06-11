package com.formentor.magnolia.ai.training.infrastructure.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formentor.magnolia.ai.training.infrastructure.openai.dto.File;
import com.formentor.magnolia.ai.training.infrastructure.openai.dto.FineTuneRequest;
import com.formentor.magnolia.ai.training.infrastructure.openai.dto.FineTuneResult;
import com.machinezoo.noexception.Exceptions;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class OpenAiApi {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    private final String host;
    private final String token;

    public OpenAiApi(String host, String token) {
        this.host = host;
        this.token = token;
    }

    public CompletableFuture<File> uploadFile(String purpose, java.io.File file) throws IOException {
        try (Response response = client.newCall(buildFileRequest(purpose, file)).execute()) {
            int code = response.code();
            String body = response.body().string();
            if (code != 200) {
                throw new RuntimeException(String.format("%s - %s", code, body));
            }

            return CompletableFuture.completedFuture(mapper.readValue(body, File.class));
        }
    }

    public CompletableFuture<FineTuneResult> createFineTune(FineTuneRequest request) throws IOException {
        RequestBody body = RequestBody.create(Exceptions.wrap().get(() -> mapper.writeValueAsString(request)), JSON);
        Request httpRequest = requestBuilder("/v1/fine_tuning/jobs")
                .post(body)
                .build();
        try (Response response = client.newCall(httpRequest).execute()) {
            int code = response.code();
            String responseBody = response.body().string();
            if (code != 200) {
                throw new RuntimeException(String.format("%s - %s", code, body));
            }

            return CompletableFuture.completedFuture(mapper.readValue(responseBody, FineTuneResult.class));
        }
    }

    private Request buildFileRequest(String purpose, java.io.File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("purpose", purpose)
                .addFormDataPart("file", "file.jsonl",
                        RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        return requestBuilder("/v1/files")
                .post(requestBody)
                .build();
    }

    private Request.Builder requestBuilder(String resource) {
        return new Request.Builder()
                .url(host + resource)
                .header("Authorization", String.format("Bearer %s", token));
    }
}
