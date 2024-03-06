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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test JsonConverter class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class JsonConverterTest {

    @Test
    public void jsonNodeToStringFailTest() throws Exception {
        ObjectMapper mockedMapper = spy(ObjectMapper.class);

        when(mockedMapper.writeValueAsString(any(ObjectNode.class)))
                .thenThrow(new JsonProcessingException("Test Exception") {});

        JsonNode jsonNode = mockedMapper.createObjectNode();

        JsonParamError thrown = assertThrows(JsonParamError.class,
                () -> JsonConverter.jsonNodeToString(mockedMapper, jsonNode, "test"));

        assertEquals("The 'test' JSON Object could not be encoded as a String.",
                thrown.getMessage());
    }

    @Test
    public void stringToJsonNodeFailTest() throws Exception {
        ObjectMapper mockedMapper = new ObjectMapper();

        // Intentionally Invalid JSON
        String jsonString = "{{{";

        JsonParamError thrown = assertThrows(JsonParamError.class,
                () -> JsonConverter.stringToJsonNode(mockedMapper, jsonString, "test"));

        assertEquals("The 'test' JSON String could not be decoded to JsonNode.",
                thrown.getMessage());
    }
}
