package com.server.back.movie.service;

import com.server.back.movie.controller.MovieController;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {
    private final WebClient.Builder webClientBuilder;
    @Getter
    private WebClient wcKobis;
    @Getter
    private WebClient wcTmdb;

    @Value("${KOBIES_KEY}")
    private String kobiesKey;
    @Value("${TMDB_ACCESS_TOKEN}")
    private String tmdbAccessToken;

    private String img_url = "https://image.tmdb.org/t/p/original";

    public MovieService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void init() {
        this.wcKobis = webClientBuilder.baseUrl("http://www.kobis.or.kr/kobisopenapi/webservice/rest").build();
        this.wcTmdb = webClientBuilder.baseUrl("https://api.themoviedb.org/3")
                .defaultHeader("accept", "application/json")
                .defaultHeader("Authorization", "Bearer "+tmdbAccessToken)
                .build();
    }

    public Mono<Map> getMovieList() {
        return getWcKobis().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/searchMovieList.json")
                        .queryParam("key", kobiesKey)
                        .queryParam("itemPerPage", "10")
                        .build())
                .retrieve()
                .bodyToMono(Map.class);
    }

    public void getDailyMovies(String nowDate) {
        System.out.println(tmdbAccessToken);
        Mono<Map> result = getWcKobis().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/boxoffice/searchDailyBoxOfficeList.json")
                        .queryParam("key",kobiesKey)
                        .queryParam("targetDt",nowDate)
                        .build())
                .retrieve()
                .bodyToMono(Map.class);

        result.subscribe(r-> {
           Map<String, Object> dailyBoxOfficeList = (Map<String,Object>) r.get("boxOfficeResult");
           System.out.println(dailyBoxOfficeList);
           if (dailyBoxOfficeList!=null) {
               List<Map<String,String>> movieList = (List<Map<String,String>>)dailyBoxOfficeList.get("dailyBoxOfficeList");
               for (Map<String,String> movie: movieList) {
                   searchNameMovie(movie.get("movieNm")).subscribe(s->{
                       Map<String,Object> searchMovie = ((List<Map<String,Object>>)s.get("results")).get(0);
                       System.out.printf("id: %s",searchMovie.get("id")+"\n");
                       System.out.printf("제목: %s",searchMovie.get("original_title")+"\n");
                       System.out.println(img_url+searchMovie.get("poster_path"));
                   });
               }
           }
        });
    }

    // 우선은 가장 상단에 있는 영화 return
    public Mono<Map> searchNameMovie(String movieName) {
        return getWcTmdb().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/movie")
                        .queryParam("query", movieName)
                        .queryParam("include_adult","false")
                        .queryParam("language","ko-KR")
                        .queryParam("page","1") // 무한 스크롤 구현해서 results 가 없을 때까지 돌리기
                        .build()
                )
                .retrieve()
                .bodyToMono(Map.class);
    }
}

