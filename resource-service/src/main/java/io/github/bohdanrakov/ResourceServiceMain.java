package io.github.bohdanrakov;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import java.util.List;

@SpringBootApplication
public class ResourceServiceMain {

    @Autowired
    private DiscoveryClient discoveryClient;

    static void main(String[] args) {
        SpringApplication.run(ResourceServiceMain.class, args);
    }

    @Bean
    public RestClient songsRestClient() {
        List<ServiceInstance> songSevices = discoveryClient.getInstances("song-service");
        String songServiceURI = songSevices.get(0).getUri().toString();
        System.out.println(songServiceURI);
        return RestClient.builder()
                .baseUrl(songServiceURI)
                .build();
    }
}
