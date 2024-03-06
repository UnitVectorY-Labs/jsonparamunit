/*
 * Copyright 2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package com.unitvectory.jsonparamunit;

/**
 * The JsonParamError exception used to indicate an assertion failed.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class JsonParamError extends AssertionError {


    /**
     * Creates a new instance of the JsonParamError class.
     * 
     * @param message the message
     */
    public JsonParamError(String message) {
        super(message);
    }

    /**
     * Creates a new instance of the JsonParamError class.
     * 
     * @param message the message
     * @param cause the cause
     */
    public JsonParamError(String message, Throwable cause) {
        super(message, cause);
    }
}
