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
import java.nio.file.Files;
import java.nio.file.Path;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * JSON based parameterized test case that provides the input and output as JsonNode objects.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public abstract class JsonNodeParamUnit {

    @Getter(value = AccessLevel.PROTECTED)
    private final JsonParamUnitConfig config;

    /**
     * Constructs a new instance of the JsonNodeParamUnit class using the default mapper.
     */
    public JsonNodeParamUnit() {
        this(JsonParamUnitConfig.builder().build());
    }

    /**
     * Constructs a new instance of the JsonNodeParamUnit class.
     * 
     * @param config the config
     */
    public JsonNodeParamUnit(JsonParamUnitConfig config) {
        this.config = config;
    }

    /**
     * The method to run the test case given the path to the JSON file to use as input for the test
     * case. The input file is assumed with the follwoing format:
     * 
     * <pre>
     * {@code
     * {
     *   "input": {
     *     "yourPayloadHere": 1
     *   },
     *   "context": "myContextValue",
     *   "output": {
     *     "yourOutputToCompareHere": "foo"
     *   }
     * }
     * }
     * </pre>
     * 
     * The "input" JSON Object is what will be parsed and provided as the input. The "context" is an
     * optional string that can provide a way to give a reference value outside of the input that
     * can be used to modify the processing. The "output" JSON Object is what will be copared to the
     * output.
     * 
     * @param filePath the file path
     */
    public final void run(String filePath) {

        JsonNode rootNode = null;
        try {
            // Load the file path to string
            String file = Files.readString(Path.of(filePath));

            // Parse the JSON
            rootNode = this.config.getMapper().readTree(file);
        } catch (Exception e) {
            fail("Failed to parse JSON", e);
        }

        // THe input object
        if (!rootNode.has("input")) {
            fail("Input is missing");
        }

        JsonNode inputNode = rootNode.get("input");

        // The optional context
        String context = rootNode.has("context") ? rootNode.get("context").asText() : null;

        // The output object
        if (!rootNode.has("output")) {
            fail("Output is missing");
        }

        JsonNode expectedOutputNode = rootNode.get("output");

        // The output must also be encoded as a string to utilize JSONAssert
        String expectedOutput;
        try {
            expectedOutput = this.config.getMapper().writeValueAsString(expectedOutputNode);
        } catch (JsonProcessingException e) {
            fail("failed to encode expected output", e);
            return;
        }

        // Process the input to produce the actual output
        JsonNode actualOutputNode = process(inputNode, context);
        String actualOutput;
        try {
            actualOutput = this.config.getMapper().writeValueAsString(actualOutputNode);
        } catch (JsonProcessingException e) {
            fail("failed to encode the actual output", e);
            return;
        }

        // Assert the actual output matches the expected output
        // This is the actual purpose of the test case.
        try {
            JSONAssert.assertEquals(expectedOutput, actualOutput, this.config.isStrictOutput());
        } catch (JSONException e) {
            fail("failed to assert the actual output matches the expected output", e);
        }
    }

    /**
     * Process the input given the context
     * 
     * @param input the input
     * @param context the context
     * @return the output
     */
    public abstract JsonNode process(JsonNode input, String context);
}
