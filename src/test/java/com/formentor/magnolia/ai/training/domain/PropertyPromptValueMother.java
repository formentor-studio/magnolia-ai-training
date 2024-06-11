package com.formentor.magnolia.ai.training.domain;

public class PropertyPromptValueMother {
    public static PropertyPromptValue fromName(String propertyName) {
        PropertyPromptValue propertyPrompt = new PropertyPromptValue();
        propertyPrompt.setPropertyName(propertyName);

        return propertyPrompt;
    }

}
