package com.springboot.springboot;

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
    @GetMapping("/json")
    public ResponseEntity<String> getJsonString(@RequestParam String objName, @RequestParam String attributeName,
                                                @RequestParam(required = false, defaultValue = "false") boolean receiveWholeObject,
                                                @RequestParam(required = false, defaultValue = "false") boolean retrieveWholeFile) throws IOException {
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/data.json");
        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (retrieveWholeFile) {
            return ResponseEntity.ok().body(jsonString);
        }
        JsonNode rootNode = new ObjectMapper().readTree(jsonString);
        JsonNode objNode = rootNode.get(objName);
        if (objNode == null) {
            return ResponseEntity.notFound().build();
        }
        if (receiveWholeObject) {
            String json = new ObjectMapper().writeValueAsString(objNode);
            return ResponseEntity.ok().body(json);
        }
        JsonNode attrNode = objNode.get(attributeName);
        if (attrNode == null) {
            return ResponseEntity.notFound().build();
        }
        String json = new ObjectMapper().writeValueAsString(attrNode);
        return ResponseEntity.ok().body(json);
    }
}
