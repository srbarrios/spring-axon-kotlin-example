package com.casumo.test.videostore.repository

import com.casumo.test.videostore.entity.RentedFilm
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RentedFilmRepository : JpaRepository<RentedFilm, Long>
