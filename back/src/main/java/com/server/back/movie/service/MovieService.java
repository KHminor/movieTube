package com.server.back.movie.service;

import com.server.back.movie.dto.ReqBoxOffice;
import com.server.back.movie.entity.BoxOfficeEntity;
import com.server.back.movie.repository.BoxOfficeRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:config.properties")
public class MovieService {
    private final WebClient.Builder webClientBuilder;
    private final BoxOfficeRepository boxOfficeRepository;
    private final Map<String,Integer> roma_dic;
    @Getter
    private WebClient wcKobis;
    @Getter
    private WebClient wcTmdb;

    @Value("${KOBIES_KEY}")
    private String kobiesKey;
    @Value("${TMDB_ACCESS_TOKEN}")
    private String tmdbAccessToken;

    private String img_url = "https://image.tmdb.org/t/p/original";

    public MovieService(WebClient.Builder webClientBuilder, BoxOfficeRepository boxOfficeRepository) {
        this.webClientBuilder = webClientBuilder;
        this.boxOfficeRepository = boxOfficeRepository;
        this.roma_dic = new HashMap<>();
        roma_dic.put("Ⅰ", 1);
        roma_dic.put("Ⅱ", 2);
        roma_dic.put("Ⅲ", 3);
        roma_dic.put("Ⅳ", 4);
        roma_dic.put("Ⅴ", 5);
        roma_dic.put("Ⅵ", 6);
        roma_dic.put("Ⅶ", 7);
        roma_dic.put("Ⅷ", 8);
        roma_dic.put("Ⅸ", 9);
        roma_dic.put("Ⅹ", 10);
    }

    @PostConstruct
    public void init() {
        this.wcKobis = webClientBuilder.baseUrl("http://www.kobis.or.kr/kobisopenapi/webservice/rest")
                .filter((request, next)-> {
                    URI originalUri = request.url();
                    URI updateUri = UriComponentsBuilder.fromUri(originalUri)
                            .queryParam("key", kobiesKey)
                            .build(true)
                            .toUri();
                    ClientRequest updatedRequest = ClientRequest.from(request) // 수정된 URI로 요청 업데이트
                            .url(updateUri)
                            .build();
                    return next.exchange(updatedRequest); // 업데이트된 요청을 다음 필터나 실제 호출로 전달
                })
                .build();
        this.wcTmdb = webClientBuilder.baseUrl("https://api.themoviedb.org/3")
                .defaultHeader("accept", "application/json")
                .defaultHeader("Authorization", "Bearer "+tmdbAccessToken)
                .build();
    }

    public Mono<Map> getMovieList() {
        return getWcKobis().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/searchMovieList.json")
//                        .queryParam("key", kobiesKey)
                        .queryParam("itemPerPage", "10")
                        .build())
                .retrieve()
                .bodyToMono(Map.class);
    }

    public List<ReqBoxOffice> getBoxOffice(String nowDate) {

        // 1. Kobis API 요청 및 응답 처리
        Map response = getWcKobis().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/boxoffice/searchDailyBoxOfficeList.json")
//                        .queryParam("key", kobiesKey)
                        .queryParam("targetDt", nowDate)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // 동기 방식으로 결과를 기다림

        // 2. dailyBoxOfficeList 추출
        Map<String, Object> dailyBoxOfficeList = (Map<String, Object>) response.get("boxOfficeResult");
        if (dailyBoxOfficeList == null) {
            throw new RuntimeException("박스오피스 데이터 조회 실패");
        }

        List<Map<String, String>> movieList = (List<Map<String, String>>) dailyBoxOfficeList.get("dailyBoxOfficeList");

        List<BoxOfficeEntity> officeEntityList = new ArrayList<>();

        // 3. 각 영화에 대해 TMDB API 호출 및 처리
        for (Map<String, String> movie : movieList) {
            Map<String, Object> searchResponse = searchNameTMMovie(movie.get("movieNm")).block(); // 동기 방식으로 결과를 기다림

            List<Map<String, Object>> results = (List<Map<String, Object>>) searchResponse.get("results");
            if (results == null || results.isEmpty()) {
                String movieNm = movie.get("movieNm");
                int idx = movieNm.length();
                if (roma_dic.containsKey(String.valueOf(movieNm.charAt(idx-1)))) { // 로마 숫자 판별
                    movieNm = movieNm.substring(0,idx-1)+roma_dic.get(String.valueOf(movieNm.charAt(idx-1)));
                    searchResponse = searchNameTMMovie(movieNm).block();
                    results = (List<Map<String, Object>>) searchResponse.get("results");
                } else {
                    boolean state = true;
                    for (int i=idx-1; i>=0; i--) {
                        if (String.valueOf(movieNm.charAt(i)).matches("\\d")) idx=i;
                        else {
                            if (idx!=i) {
                                movieNm = movieNm.substring(0,idx)+" "+movieNm.substring(idx);
                                searchResponse = searchNameTMMovie(movieNm).block();
                                results = (List<Map<String, Object>>) searchResponse.get("results");
                            }
                            else {
                                System.out.println("No search results found for movie: " + movieNm);
                                state = false;
                            }
                            break;
                        }
                    }
                    if (!state) continue;
                }
            }
            if (results!=null&&!results.isEmpty()) {
                Map<String, Object> searchMovie = results.get(0); // 첫 번째 결과 사용
                BoxOfficeEntity boxOfficeEntity = BoxOfficeEntity.builder()
                        .tmRank(movie.get("rank"))
                        .kMovieCd(movie.get("movieCd"))
                        .movieNm(movie.get("movieNm"))
                        .openDt(movie.get("openDt"))
                        .audiAcc(movie.get("audiAcc"))
                        .rankOldAndNew(movie.get("rankOldAndNew"))
                        .tmId((int) searchMovie.get("id"))
                        .overView((String) searchMovie.get("overview"))
                        .posterPath(img_url + searchMovie.get("poster_path"))
                        .backdropPath(img_url + searchMovie.get("backdrop_path"))
                        .build();
                officeEntityList.add(boxOfficeEntity);
            }
        }

        // 4. 데이터 저장
        List<BoxOfficeEntity> savedEntities = boxOfficeRepository.saveAll(officeEntityList);

        // 5. 반환 데이터 변환
        return savedEntities.stream()
                .map(movie -> ReqBoxOffice.builder()
                        .tmRank(movie.getTmRank())
                        .kMovieCd(movie.getKMovieCd())
                        .movieNm(movie.getMovieNm())
                        .openDt(movie.getOpenDt())
                        .audiAcc(movie.getAudiAcc())
                        .rankOldAndNew(movie.getRankOldAndNew())
                        .tmId(movie.getTmId())
                        .overView(movie.getOverView())
                        .posterPath(movie.getPosterPath())
                        .backdropPath(movie.getBackdropPath())
                        .build())
                .collect(Collectors.toList());
    }

    // 우선은 가장 상단에 있는 영화 return
    public Mono<Map> searchNameTMMovie(String movieName) {
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


    // kobis에서 영화 제목으로 찾기
    public void searchNameKobisMovie(String movieName) {
        int maxRow = 40;
        Map response = getWcKobis().get()
                .uri(uriBuilder-> uriBuilder
                        .path("/movie/searchMovieList.json")
                        .queryParam("itemPerPage",maxRow) // 우선은 최대 40개 정도 조회하기
                        .queryParam("movieNm",movieName)
                        .build()
                ).retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> searchMovieList = (Map<String,Object>)response.get("movieListResult");
        if (searchMovieList!=null) throw new RuntimeException("검색한 영화의 데이터 조회 실패");

        
    }
}

