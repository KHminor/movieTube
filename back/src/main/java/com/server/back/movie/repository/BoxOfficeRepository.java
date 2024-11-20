package com.server.back.movie.repository;

import com.server.back.movie.entity.BoxOfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BoxOfficeRepository extends JpaRepository<BoxOfficeEntity,Long> {
}
