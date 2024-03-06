/*
 * Copyright 2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package com.unitvectory.jsonparamunit;

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
        this(JsonParamUnitConfig.builder().build());
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
    protected final JsonNode process(JsonNode input, String context) {
        String inputString =
                JsonConverter.jsonNodeToString(this.getConfig().getMapper(), input, "input");
        String outputString = process(inputString, context);
        return JsonConverter.stringToJsonNode(this.getConfig().getMapper(), outputString, "output");
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
    protected abstract String process(String input, String context);

}
