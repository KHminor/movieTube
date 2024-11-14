package com.server.back.movie.kobis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class MovieService {

    private final WebClient webClient;

    public MovieService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie").build();
    }

    public Mono<Map> getMovieList(String kobies_key) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/searchMovieList.json")
                        .queryParam("key", kobies_key)
                        .queryParam("itemPerPage", "10")
                        .build())
                .retrieve()
                .bodyToMono(Map.class);
    }
}
