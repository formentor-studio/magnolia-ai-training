package com.formentor.magnolia.ai.training.domain;

import lombok.Data;

import java.util.List;

@Data
public class Dataset {
    private List<Example> examples;
}
