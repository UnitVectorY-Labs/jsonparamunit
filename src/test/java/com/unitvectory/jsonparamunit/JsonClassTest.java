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
import com.unitvectory.jsonparamunit.example.InClass;
import com.unitvectory.jsonparamunit.example.OutClass;

/**
 * Test implementation of the JsonClassParamUnit class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class JsonClassTest extends JsonClassParamUnit<InClass, OutClass> {

    /**
     * Creates a new instance of the JsonClassTest.
     * 
     * This specifies the input class as InClass.
     */
    protected JsonClassTest() {
        super(InClass.class);
    }

    @ParameterizedTest
    @ListFileSource(resources = "/files", fileExtension = ".json", recurse = true)
    public void testIt(String file) {
        // Utilize jsonparamunit to have a test case for each JSON file
        run(file);
    }

    @Override
    public OutClass process(InClass input, String context) {
        if (input.getFoo() == 0) {
            return OutClass.builder().success(false).build();
        } else if (input.getFoo() == 1) {
            return OutClass.builder().success(true).build();
        }

        return null;
    }
}
