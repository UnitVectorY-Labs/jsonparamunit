[![License](https://img.shields.io/badge/License-EPL%202.0-blue.svg)](https://www.eclipse.org/legal/epl-v20.html) [![Active](https://img.shields.io/badge/Status-Active-green)](https://unitvectory-labs.github.io/uvy-labs-guide/bestpractices/status.html#active) [![Maven Central](https://img.shields.io/maven-central/v/com.unitvectory/jsonparamunit)](https://central.sonatype.com/artifact/com.unitvectory/jsonparamunit) [![javadoc](https://javadoc.io/badge2/com.unitvectory/jsonparamunit/javadoc.svg)](https://javadoc.io/doc/com.unitvectory/jsonparamunit) [![codecov](https://codecov.io/gh/UnitVectorY-Labs/jsonparamunit/graph/badge.svg?token=EAJEIFCXYU)](https://codecov.io/gh/UnitVectorY-Labs/jsonparamunit)

# jsonparamunit

Library for creating parameterized JUnit 5 tests based on JSON files.

## Purpose

The core objective of this library is to streamline the way developers approach unit testing in Java through the use of data driven test cases. By leveraging JSON for defining test parameters, including inputs and expected outputs, `jsonparamunit` aims to simplify the process of creating and maintaining test cases.

This approach allows developers to focus on the logic and behavior of the code under test, rather than the mechanics of test case setup. When combined with [fileparamunit](https://github.com/UnitVectorY-Labs/fileparamunit) the test cases are defined as JSON with a single Java class calling the method to test.

Designed for ease of integration into existing Java 17+ and JUnit 5 projects, this library aims to increase code coverage by shifting code from

## Getting Started

This library requires Java 17 and JUnit 5 and is available in the Maven Central Repository:

```xml
<dependency>
    <groupId>com.unitvectory</groupId>
    <artifactId>jsonparamunit</artifactId>
    <version>0.0.5</version>
    <scope>test</scope>
</dependency>
```

## Usage

The goal of this library is to utilize JSON as a means of defining test cases. Instead of writing Java code, JSON files are used with the following structure:

```json
{
  "input": {
    "value": "1234567890"
  },
  "context": "optionalContext",
  "output": {
    "value": "0987654321"
  }
}
```

- The `input` is an arbitrary JSON object representing the input of the test case
- The `context` is an optional JSON string that can be used as additional context in processing that is not part of the input
- The `output` is an arbitrary JSON object representing the expected output of the test case; the output will be compared to the actual output of the test case using [JSON Assert](https://github.com/skyscreamer/JSONassert) acting as the core of each test case

There are 3 classes that can be used to create the test cases through extension that operate on the JSON Object at different levels of parsing:

`JsonStringParamUnit`: Input is passed a String containing the encoded JSON and output expects a String with the output JSON encoded.

```java
String process(String input, String context)
```

`JsonNodeParamUnit`: Input is passed as a Jackson JsonNode and output is expected as a JsonNode allowing for flexible handling of the input and output.

```java
JsonNode process(JsonNode input, String context)
```

`JsonClassParamUnit`: Input and output are both handled as POJOs utilziing Jackson to handle the serliziation and deserialization from the input and output. A complete example follows giving the following example class being used as both the input and output.

```java
package example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestObject {

    private String value;
}
```

The example shown here is using reversing a string as the example, but in practice the input and output are expected to be more complex objects exercising the logic of the code under test.

This example also uses [fileparamunit](https://github.com/UnitVectorY-Labs/fileparamunit) to iterate through all of the JSON files contained in the `/strings` directory under the test resources allowing additional test cases to be created without modifying the Java code.

```xml
<dependency>
    <groupId>com.unitvectory</groupId>
    <artifactId>fileparamunit</artifactId>
    <version>0.0.4</version>
    <scope>test</scope>
</dependency>
```

The actual execution of a test is performed by calling the `run` method passing in the path to the JSON file with the test case.

```java
package example;

import org.junit.jupiter.params.ParameterizedTest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unitvectory.fileparamunit.ListFileSource;
import com.unitvectory.jsonparamunit.JsonClassParamUnit;

public class ReverseStringTest extends JsonClassParamUnit<TestObject, TestObject> {

    @ParameterizedTest
    @ListFileSource(resources = "/strings/", fileExtension = ".json", recurse = false)
    public void testIt(String file) {
        // Utilize jsonparamunit to have a test case for each JSON file
        run(file);
    }

    protected ReverseStringTest() {
        super(InputString.class, JsonParamUnitConfig.builder().build());
    }

    @Override
    public TestObject process(TestObject input, String context) {
        // This represents the functionality you want to test, in this example the functionality is
        // just reversing a string but in real life this would be a complex operation to unit test
        String reversedString = new StringBuilder(input.getValue()).reverse().toString();
        return TestObject.builder().value(reversedString).build();
    }
}
```

The `JsonParamUnitTest` can be provided as a constructor parameter to be able to customize the behavior of the test case.

- The Jackson ObjectMapper can be provided using the `mapper` parameter.
- The JSON Assert behavior defaults to strict which is highly recommended but can be changed using the `strictOutput` parameter.
