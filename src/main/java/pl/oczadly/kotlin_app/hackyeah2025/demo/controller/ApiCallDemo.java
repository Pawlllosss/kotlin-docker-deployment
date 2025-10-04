package pl.oczadly.kotlin_app.hackyeah2025.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ApiCallDemo {

    public String makeDemoApiCall() {
        return RestClient.builder()
            .baseUrl("https://aviation-api.imgw.pl/data/")
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
            .build()
            .get()
            .uri("last?params=gamet&format=json")
            .retrieve()
            .body(String.class);
    }
}
