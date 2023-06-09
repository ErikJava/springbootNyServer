package com.springboot.springboot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.aot.hint.TypeReference;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    // This maps an HTTP GET request with the path "/json" to the getJsonString method.
    @GetMapping("/json")
    //  This is the method signature. It takes two parameters, objName and retrieveWholeFile, both of which are optional query parameters.
    public ResponseEntity<String> getJsonString(@RequestParam(required = false) String objName,
                                                @RequestParam(required = false, defaultValue = "false") boolean retrieveWholeFile) throws IOException {
        // Read the data.json file as an InputStream
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/data.json");

        // Convert the InputStream to a String
        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (retrieveWholeFile) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);
            return ResponseEntity.ok().body(mapper.writeValueAsString(mapper.readValue(jsonString, Object.class)));
        }
        if (objName == null || objName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Parse the JSON string and retrieve the JSON object specified by objName
        JsonNode rootNode = new ObjectMapper().readTree(jsonString);
        JsonNode objNode = rootNode.get(objName);
        if (objNode == null) {
            return ResponseEntity.ok().body("null");
        }

        // Convert the JSON object to a JSON string and return a response with the body containing the string
        String json = new ObjectMapper().writeValueAsString(objNode);
        return ResponseEntity.ok().body(json);
    }
}



