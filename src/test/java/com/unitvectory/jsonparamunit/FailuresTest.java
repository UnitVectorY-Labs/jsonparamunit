/*
 * Copyright 2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package com.unitvectory.jsonparamunit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.unitvectory.fileparamunit.ListFileSource;

/**
 * Test cases for testing the failures by intentionally misconfiguring the test case environment and
 * asserting the correct exception is thrown to describe the failure.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class FailuresTest extends JsonNodeParamUnit {

    @Test
    public void filePathNullTest() {
        JsonParamError exception = assertThrows(JsonParamError.class, () -> {
            run(null);
        });

        assertEquals("The provided filePath is null.", exception.getMessage());
    }

    @Test
    public void filePathEmptyTest() {
        JsonParamError exception = assertThrows(JsonParamError.class, () -> {
            run("");
        });

        assertEquals("The provided filePath is empty.", exception.getMessage());
    }

    @ParameterizedTest
    @ListFileSource(resources = "/failures/badjson", fileExtension = ".json")
    public void fileNotExistTest(String file) {
        JsonParamError exception = assertThrows(JsonParamError.class, () -> {
            // Changing the file path it won't exist
            run(file + "x");
        });

        assertEquals("The provided filePath does not exist.", exception.getMessage());
    }

    @ParameterizedTest
    @ListFileSource(resources = "/failures/badjson", fileExtension = ".json")
    public void fileIsDirectoryTest(String file) {
        // For this test instead of the file we are getting the directory it is in which will fail
        String directory = new File(file).getParent();
        JsonParamError exception = assertThrows(JsonParamError.class, () -> {
            // Changing the file path it won't exist
            run(directory);
        });

        assertEquals("The provided filePath is not a regular file.", exception.getMessage());
    }

    @ParameterizedTest
    @ListFileSource(resources = "/failures/badjson", fileExtension = ".json")
    public void badJsonTest(String file) {
        JsonParamError exception = assertThrows(JsonParamError.class, () -> {
            // Changing the file path it won't exist
            run(file);
        });

        assertEquals("Failed to parse the JSON from the test file.", exception.getMessage());
    }

    @ParameterizedTest
    @ListFileSource(resources = "/failures/noinput", fileExtension = ".json")
    public void noInputTest(String file) {
        JsonParamError exception = assertThrows(JsonParamError.class, () -> {
            run(file);
        });

        assertEquals("The 'input' JSON Object is missing from the test file.",
                exception.getMessage());
    }

    @ParameterizedTest
    @ListFileSource(resources = "/failures/nooutput", fileExtension = ".json")
    public void noOutputTest(String file) {
        JsonParamError exception = assertThrows(JsonParamError.class, () -> {
            run(file);
        });

        assertEquals("The 'output' JSON Object is missing from the test file.",
                exception.getMessage());
    }

    @Override
    public JsonNode process(JsonNode input, String context) {
        return this.getConfig().getMapper().createObjectNode();
    }

}
