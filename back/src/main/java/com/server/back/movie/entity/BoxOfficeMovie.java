package com.server.back.movie.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BoxOfficeMovie {
    @Id
    private long id;

    private String kMovieCd;
    private String rank;
    private String movieNm;
    private String openDt;
    private String audiAcc; // 누적 관객수
    private String rankOldAndNew; // 랭킹 신규 진입 OLD: 기존, NEW: 신규
    private long tmId;
    private String overView;
    private String backdropPath;
    private String posterPath;

}
