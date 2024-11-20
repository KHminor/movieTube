package com.server.back.movie.controller;

import com.server.back.movie.dto.ReqBoxOffice;
import com.server.back.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.time.*;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movies")
    public Mono<Map> getMovies() {
        Mono<Map> result = movieService.getMovieList();
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


    @GetMapping("/boxOffice")
    public List<ReqBoxOffice> getBoxOffice() {

        String nowDate = String.valueOf(LocalDate.now().minusDays(1)).replaceAll("-","");
        System.out.println(nowDate);
//        movieService.getBoxOffice("20140401");
        return movieService.getBoxOffice(nowDate);
    }


//    @GetMapping("/{movieName}")
//    public void searchNameMovie(@PathVariable("movieName") String movieName) {
//        movieService.searchNameMovie(movieName);
//    }

    @GetMapping("/searchNameMovie") 
    public void searchNameMovie(String title) {
        String movieTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        movieService.searchNameMovie(movieTitle);
    }
}
