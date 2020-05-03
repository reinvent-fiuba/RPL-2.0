package com.example.rpl.RPL.exception.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.springframework.http.HttpStatus;

class HttpStatusDeserializer extends JsonDeserializer<HttpStatus> {

    @Override
    public HttpStatus deserialize(JsonParser jsonParser,
        DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        return HttpStatus.valueOf(Integer.parseInt(node.asText()));
    }
}