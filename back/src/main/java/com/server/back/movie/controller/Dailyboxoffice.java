package com.server.back.movie.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@PropertySource("classpath:config.properties")
@Api(tags = "일별 박스오피스")
public class Dailyboxoffice {
    @Value("${KOBIES_KEY}")
    private String key;
    private String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.xml";
    private String Dt = "20241110";
    @GetMapping
    public void find() {
        System.out.printf("%s?key=%s&targetDt=%s",url,key,Dt);
        System.out.println();
    }
}
