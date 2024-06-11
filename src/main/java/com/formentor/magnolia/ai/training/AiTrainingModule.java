package com.formentor.magnolia.ai.training;

import info.magnolia.module.ModuleLifecycleContext;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration for the formentor-ai-training module.
 */
@Setter
@Getter
public class AiTrainingModule implements info.magnolia.module.ModuleLifecycle {
    private String host;
    private String baseModel;
    private Azure azure;

    @Override
    public void start(ModuleLifecycleContext moduleLifecycleContext) {

    }

    @Override
    public void stop(ModuleLifecycleContext moduleLifecycleContext) {

    }

    @Getter
    @Setter
    public static class Azure {
        private String host;
        private String apiVersion;
        private String baseModel;
    }
}
