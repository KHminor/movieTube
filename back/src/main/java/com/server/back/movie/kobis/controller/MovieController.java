package com.server.back.movie.kobis.controller;

import com.server.back.movie.kobis.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MovieController {
    @Value("${KOBIES_KEY}")
    String kobies_key;

    private final MovieService movieService;

    @GetMapping("/movies")
    public Mono<Map> getMovies() {
        Mono<Map> result = movieService.getMovieList(kobies_key);
        result.subscribe(r-> {
            Map<String, Object> movieListResult = (Map<String,Object>) r.get("movieListResult");
            if (movieListResult!=null) {
                List<Object> li = (List<Object>) movieListResult.get("movieList");
                System.out.println(li.get(0));
                System.out.println(li.get(1));
            }
        });
        return result;
    }
}
