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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unitvectory.fileparamunit.ListFileSource;

/**
 * Test based on the context.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class ContextTest extends JsonNodeParamUnit {

    @ParameterizedTest
    @ListFileSource(resources = "/context", fileExtension = ".json", recurse = true)
    public void testIt(String file) {
        // Utilize jsonparamunit to have a test case for each JSON file
        run(file);
    }

    @Override
    public JsonNode process(JsonNode input, String context) {
        // Test demonstrating the context being used as output
        ObjectNode node = this.getConfig().getMapper().createObjectNode();
        node.put("value", context);
        return node;
    }
}
