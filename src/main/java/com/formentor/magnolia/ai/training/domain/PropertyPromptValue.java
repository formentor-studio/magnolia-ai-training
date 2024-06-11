package com.formentor.magnolia.ai.training.domain;

import lombok.Data;

@Data
public class PropertyPromptValue {
    private String propertyName;
    private Integer limit;
    private Reference reference; // Inspired by Delivery API references. https://docs.magnolia-cms.com/product-docs/6.2/Developing/API/Delivery-API/Resolving-references-with-the-delivery-endpoint.html

    public boolean isReference() {
        return (reference != null && reference.getTargetWorkspace() != null && reference.getTargetPropertyName() != null);
    }

    @Data
    public static class Reference {
        private String targetWorkspace;
        private String targetPropertyName;
    }
}
