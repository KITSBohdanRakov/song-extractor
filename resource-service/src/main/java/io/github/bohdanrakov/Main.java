package io.github.bohdanrakov;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class Main {

    @Value("${songs.service.base-url}")
    private String songsServiceBaseUrl;

    static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public RestClient songsRestClient() {
        return RestClient.builder()
                .baseUrl(songsServiceBaseUrl)
                .build();
    }
}
