package com.luizmarinho.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizmarinho.literalura.model.Book;

import java.util.ArrayList;
import java.util.List;

public class DataConversion {
    ObjectMapper mapper = new ObjectMapper();

    public List<Book> convertFromJson(String json) {
        List<Book> books = new ArrayList<>();
        try {
            JsonNode response = mapper.readTree(json);
//            System.out.println("********** COUNT *************\n" + response.path("count").asInt());
            if (response.path("count").asInt() > 0) {
                JsonNode results = response.findPath("results");
                for (JsonNode j : results) {
                    books.add(mapper.readValue(j.toString(), Book.class));
                }
            } else {
                System.out.println("**** Book not found ****");
            }
        } catch(JsonProcessingException e) {
            System.out.println("Error to convert from json");
        }
        return books;
    }
}
