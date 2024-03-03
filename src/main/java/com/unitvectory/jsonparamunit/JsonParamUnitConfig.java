/*
 * Copyright 2024 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */
package com.unitvectory.jsonparamunit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Value;

/**
 * Configuration for customizing the behavior.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Value
@Builder
public class JsonParamUnitConfig {

    @Builder.Default
    private final ObjectMapper mapper = new ObjectMapper();

    @Builder.Default
    private final boolean strictOutput = true;
}
