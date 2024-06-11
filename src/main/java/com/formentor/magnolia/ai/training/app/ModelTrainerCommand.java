package com.formentor.magnolia.ai.training.app;

import info.magnolia.commands.MgnlCommand;
import info.magnolia.context.Context;
import lombok.Setter;
import com.formentor.magnolia.ai.training.application.AiModelTrainer;
import com.formentor.magnolia.ai.training.domain.PropertyPromptValue;

import java.util.List;

public class ModelTrainerCommand extends MgnlCommand {
    private final AiModelTrainer modelTrainer;

    @Setter
    private String modelName;

    @Setter
    String workspace;

    @Setter
    String root;

    @Setter
    String nodeType;

    @Setter
    List<PropertyPromptValue> propertiesAsPrompt;

    @Setter
    String propertyAsCompletion;

    public ModelTrainerCommand(AiModelTrainer modelTrainer) {
        this.modelTrainer = modelTrainer;
    }

    @Override
    public boolean execute(Context context) throws Exception {
        modelTrainer.run(modelName, context.getJCRSession(workspace), root, nodeType, propertiesAsPrompt , propertyAsCompletion).join();

        return true;
    }
}
