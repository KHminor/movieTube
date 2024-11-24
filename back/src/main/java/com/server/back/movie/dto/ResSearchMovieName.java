package com.server.back.movie.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResSearchMovieName {
    private String movieNm;
}
