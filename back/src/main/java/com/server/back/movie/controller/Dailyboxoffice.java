package com.server.back.movie.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

@RestController
@PropertySource("classpath:config.properties")
@Api(tags = "일별 박스오피스")
public class Dailyboxoffice {
    @Value("${KOBIES_KEY}")
    private String key;
    @Value("${TMDB_ACCESS_TOKEN}")
    private String tm_key2;
    private String daily = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.xml";
    private String url2 = "https://api.themoviedb.org/3/movie";
    private String img_url = "https://image.tmdb.org/t/p/original";
    private String Dt = "20241112";
    @GetMapping("/kobis")
    public void find() throws IOException, InterruptedException {
//        System.out.printf("%s?key=%s&targetDt=%s",url,key,Dt);
        System.out.println();




    }


//    @GetMapping("/tmdb-data")
//    public void getTmdbData() {
//        System.out.println(String.format("https://api.themoviedb.org/3/movie/top_rated?api_key=%s",key2));
//    }

    @GetMapping("/tmdb-data")
    public void getTmdbData() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+tm_key2)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());

        // JSON 파싱
        JSONObject jsonObject = new JSONObject(response.body());

        // results 배열 가져오기
        JSONArray results = jsonObject.getJSONArray("results");

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        // 특정 인덱스의 영화 데이터 가져오기 (예: 첫 번째 영화)
//        JSONObject firstMovie = results.getJSONObject(0);

        for (int i=0; i<results.length(); i++) {
            bw.write("===========\n");
            bw.write(String.format("id: %s",results.getJSONObject(i).getInt("id")+"\n"));
            bw.write(String.format("original_titile: %s",results.getJSONObject(i).getString("title")+"\n"));
            bw.write(String.format("title: %s",results.getJSONObject(i).getString("title")+"\n"));
            bw.write(String.format("adult: %s",results.getJSONObject(i).getBoolean("adult")+"\n"));
            bw.write(String.format("release_date: %s",results.getJSONObject(i).getString("release_date")+"\n"));
        }
        bw.flush();
        // 영화의 제목과 평점 출력
//        System.out.println("Title: " + firstMovie.getString("title"));
//        System.out.println("Vote Average: " + firstMovie.getDouble("vote_average"));
    }
}
