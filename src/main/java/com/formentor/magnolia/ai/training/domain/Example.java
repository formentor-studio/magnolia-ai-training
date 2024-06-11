package com.formentor.magnolia.ai.training.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Example {
    private String prompt;
    private String completion;
}
