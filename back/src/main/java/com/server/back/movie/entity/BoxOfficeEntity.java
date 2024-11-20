package com.server.back.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoxOfficeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String tmRank;
    private String kMovieCd; // kobise 영화ID
    private String movieNm;
    private String openDt;
    private String audiAcc; // 누적 관객수
    private String rankOldAndNew; // 랭킹 신규 진입 OLD: 기존, NEW: 신규
    private int tmId; // tmdb 영화ID
    @Lob
    private String overView; // 설명
    private String posterPath;
    private String backdropPath;

}
