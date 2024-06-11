package com.formentor.magnolia.ai.training.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Objet Mother to provide mocks for Dataset domain class.
 * See https://martinfowler.com/bliki/ObjectMother.html
 */
public class DatasetMother {
    public static Dataset fromExamples(List<Example> exampleList) {
        Dataset dataset = new Dataset();
        dataset.setExamples(exampleList);

        return dataset;
    }

    public static Dataset random() {
        Dataset dataset = new Dataset();
        dataset.setExamples(Arrays.asList(
                Example.builder()
                        .prompt("name is Vietnam: Tradition and Today. location is Ho Chi Minh City, Vietnam. duration is 14.")
                        .completion("Vietnam is one of the world's most exotic and culturally rich destinations. A gem among gems, it offers dazzling diversity for visitors.")
                        .build(),
                Example.builder()
                        .prompt("name is Hut to Hut in the Swiss Alps. location is Zurich, Switzerland. duration is 7.")
                        .completion("Some Swiss people are going to be pretty annoyed with us for revealing their secrets. Hush..don&rsquo;t tell anyone, but there&rsquo;s more to Switzerland than skiing! And we don&rsquo;t mean shopping for watches and staying in fancy hotels.")
                        .build()
        ));

        return dataset;
    }
}
