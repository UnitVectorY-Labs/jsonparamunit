/*
 * Copyright 2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package com.unitvectory.jsonparamunit;

import static org.junit.jupiter.api.Assertions.fail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * JSON based parameterized test case that provides the input and output as String of JSON.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public abstract class JsonStringParamUnit extends JsonNodeParamUnit {

    /**
     * Creates a new instance of the JsonStringParamUnit.
     */
    protected JsonStringParamUnit() {
        super(JsonParamUnitConfig.builder().build());
    }

    /**
     * Creates a new instance of the JsonStringParamUnit.
     * 
     * @param config the config
     */
    protected JsonStringParamUnit(JsonParamUnitConfig config) {
        super(config);
    }

    @Override
    public final JsonNode process(JsonNode input, String context) {

        String inputString = null;
        try {
            inputString = this.getConfig().getMapper().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            fail("failed to encode input as JsonNode", e);
        }

        String outputString = process(inputString, context);

        try {
            return this.getConfig().getMapper().readTree(outputString);
        } catch (JsonProcessingException e) {
            fail("failed to encode ouput as JsonNode", e);
            return null;
        }
    }

    /**
     * Process the input and return the output.
     * 
     * Input and output are both the String encoding of the JSON object.
     * 
     * @param input the input
     * @param context the context
     * @return the output
     */
    public abstract String process(String input, String context);

}
