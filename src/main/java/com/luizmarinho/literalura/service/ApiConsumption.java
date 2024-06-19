package com.luizmarinho.literalura.service;

import com.luizmarinho.literalura.exception.ExceptionOnApi;
import com.luizmarinho.literalura.model.Book;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApiConsumption {
    DataConversion conversion = new DataConversion();

    public List<Book> searchBook(String titleName) {
        String urlSearch = "https://gutendex.com/books/?search=" + URLEncoder.encode(titleName, StandardCharsets.UTF_8);
        HttpResponse<String> response;
        List<Book> books;

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder(URI.create(urlSearch)).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            verifyResponse(response.statusCode());
            books = conversion.convertFromJson(response.body());
            return books;
        } catch(IOException | ExceptionOnApi | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void verifyResponse(int code) throws ExceptionOnApi {
        if (code >= 400 && code < 500) {
            throw new ExceptionOnApi(code + ": Error on client");
        } else if (code >= 500) {
            throw new ExceptionOnApi(code + ": Server error happened, try again later");
        }
    }
}
