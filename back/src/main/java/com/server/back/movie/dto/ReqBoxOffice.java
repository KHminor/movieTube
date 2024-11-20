package com.server.back.movie.dto;

import com.server.back.movie.entity.BoxOfficeEntity;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Lob;

@Data
@Builder
public class ReqBoxOffice {
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
