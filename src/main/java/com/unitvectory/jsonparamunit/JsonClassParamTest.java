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
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON based parameterized test case that provides the input and output as Java Classes parsed
 * using Jackson.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public abstract class JsonClassParamTest<I, O> extends JsonNodeParamTest {

    private final Class<I> inputClass;

    /**
     * Creates a new instance of the JsonClassParamTest.
     * 
     * This is required to capture the input class I.
     * 
     * @param inputClass the input class.
     */
    protected JsonClassParamTest(Class<I> inputClass) {
        super(DEFAULT_MAPPER);
        this.inputClass = inputClass;
    }

    /**
     * Creates a new instance of the JsonClassParamTest.
     * 
     * @param inputClass the input class
     * @param mapper the mapper
     */
    protected JsonClassParamTest(Class<I> inputClass, ObjectMapper mapper) {
        super(mapper);
        this.inputClass = inputClass;
    }

    @Override
    public JsonNode process(JsonNode input, String context) {

        // Convert the input JsonNode to the class I
        I inputObject = this.getMapper().convertValue(input, inputClass);

        // Process the input, get back the output O
        O outputObject = process(inputObject, context);

        // Convert the output into a JsonNode
        return this.getMapper().convertValue(outputObject, JsonNode.class);
    }

    /****
     * Processes the input and returns the output.
     * 
     * The input and output are transformed into the specified input and output classes using
     * Jackson.
     * 
     * @param input the input.
     * @param context the context.
     * @return the output.
     */
    public abstract O process(I input, String context);

}
