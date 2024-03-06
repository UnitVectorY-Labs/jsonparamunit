/*
 * Copyright 2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package com.unitvectory.jsonparamunit;

import java.nio.file.Files;
import java.nio.file.Path;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
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
    protected final void run(String filePath) {

        if (filePath == null) {
            throw new JsonParamError("The provided filePath is null.");
        }

        if (filePath.isEmpty()) {
            throw new JsonParamError("The provided filePath is empty.");
        }

        Path path = Path.of(filePath);

        // Check if the file exists

        if (!Files.exists(path)) {
            throw new JsonParamError("The provided filePath does not exist.");
        }

        // Check if the file is a directory

        if (!Files.isRegularFile(path)) {
            throw new JsonParamError("The provided filePath is not a regular file.");
        }

        // Parse the test JSON

        JsonNode rootNode = null;
        try {
            // Load the file path to string
            String file = Files.readString(path);

            // Parse the JSON
            rootNode = this.config.getMapper().readTree(file);
        } catch (Exception e) {
            throw new JsonParamError("Failed to parse the JSON from the test file.", e);
        }

        // The input object
        if (!rootNode.has("input")) {
            throw new JsonParamError("The 'input' JSON Object is missing from the test file.");
        }

        JsonNode inputNode = rootNode.get("input");

        // The optional context
        String context = rootNode.has("context") ? rootNode.get("context").asText() : null;

        // The output object
        if (!rootNode.has("output")) {
            throw new JsonParamError("The 'output' JSON Object is missing from the test file.");
        }

        JsonNode expectedOutputNode = rootNode.get("output");

        // The output must also be encoded as a string to utilize JSONAssert
        String expectedOutput = JsonConverter.jsonNodeToString(this.config.getMapper(),
                expectedOutputNode, "output");

        // Process the input to produce the actual output
        JsonNode actualOutputNode = process(inputNode, context);
        String actualOutput = JsonConverter.jsonNodeToString(this.config.getMapper(),
                actualOutputNode, "actualOutput");

        // Assert the actual output matches the expected output
        // This is the actual purpose of the test case.
        assertJsonEquals(expectedOutput, actualOutput);
    }

    /**
     * Uses JSONAssert to verify the JSON strings are equal.
     * 
     * @param expected the expected JSON
     * @param actual the actual JSON
     */
    protected void assertJsonEquals(String expected, String actual) {
        try {
            JSONAssert.assertEquals(expected, actual, this.config.isStrictOutput());
        } catch (JSONException e) {
            throw new JsonParamError(
                    "Failed to assert the actual output matches the expected output.", e);
        }
    }

    /**
     * Process the input given the context
     * 
     * @param input the input
     * @param context the context
     * @return the output
     */
    protected abstract JsonNode process(JsonNode input, String context);
}
