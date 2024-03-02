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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unitvectory.fileparamunit.ListFileSource;
import com.unitvectory.jsonparamunit.example.InputString;
import com.unitvectory.jsonparamunit.example.OutputString;

/**
 * Test implementation that uses reversing strings as its test case.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class ReverseStringTest extends JsonClassParamTest<InputString, OutputString> {

    @ParameterizedTest
    @ListFileSource(resources = "/strings/", fileExtension = ".json", recurse = false)
    public void testIt(String file) {
        // Utilize jsonparamunit to have a test case for each JSON file
        run(file);
    }

    protected ReverseStringTest() {
        super(InputString.class, new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    @Override
    public OutputString process(InputString input, String context) {
        // This represents the functionality you want to test, in this example the functionality is
        // just reversing a string but in real life this would be a complex operation to unit test
        String reversedString = new StringBuilder(input.getValue()).reverse().toString();
        return OutputString.builder().value(reversedString).build();
    }
}
