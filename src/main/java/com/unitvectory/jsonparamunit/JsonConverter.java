/*
 * Copyright 2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package com.unitvectory.jsonparamunit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

/**
 * Utility library for converting between JsonNode and String representations of JSON.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@UtilityClass
class JsonConverter {

    /**
     * Convert JsonNode to String
     * 
     * @param objectMapper the Jackson ObjectMapper
     * @param jsonNode the JsonNode to encode
     * @param fieldName the file name used for error logging
     * @return the encoded String
     */
    public static String jsonNodeToString(ObjectMapper objectMapper, JsonNode jsonNode,
            String fieldName) {
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new JsonParamError(
                    "The '" + fieldName + "' JSON Object could not be encoded as a String.", e);
        }
    }

    /**
     * Convert String to JsonNode
     * 
     * @param objectMapper the Jackson ObjectMapper
     * @param jsonString the String to decode
     * @param fieldName the field name used for error logging
     * @return the decoded JsonNode
     */
    public static JsonNode stringToJsonNode(ObjectMapper objectMapper, String jsonString,
            String fieldName) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new JsonParamError(
                    "The '" + fieldName + "' JSON String could not be decoded to JsonNode.", e);
        }
    }
}
