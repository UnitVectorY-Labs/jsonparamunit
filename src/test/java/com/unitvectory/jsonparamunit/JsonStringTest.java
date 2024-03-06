/*
 * Copyright 2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package com.unitvectory.jsonparamunit;

import org.junit.jupiter.params.ParameterizedTest;
import com.unitvectory.fileparamunit.ListFileSource;

/**
 * Test implementation of the JsonStringParamUnit class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class JsonStringTest extends JsonStringParamUnit {

    public JsonStringTest() {
        super(JsonParamUnitConfig.builder().build());
    }

    @ParameterizedTest
    @ListFileSource(resources = "/files", fileExtension = ".json", recurse = true)
    public void testIt(String file) {
        // Utilize jsonparamunit to have a test case for each JSON file
        run(file);
    }

    @Override
    public String process(String input, String context) {
        if (input.contains("1")) {
            return "{\"success\": true}";
        } else if (input.contains("0")) {
            return "{\"success\": false}";
        } else {
            return null;
        }
    }
}
