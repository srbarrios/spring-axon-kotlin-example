package com.casumo.test.videostore.acceptance

import com.casumo.test.videostore.constants.FilmFormat
import com.casumo.test.videostore.constants.FilmType
import com.casumo.test.videostore.entity.Film
import java.util.*
import java.util.Arrays.asList

object VideoStoreTestConstants {
    val matrixFilm = Film("1",
            "Matrix 11",
            FilmType.newFilm,
            FilmFormat.blueray,
            "ScyFi",
            asList("english", "swedish", "spanish"),
            16,
            Date(2017, 9, 6),
            "Matrix description")
    val spiderManFilm = Film("2",
            "Spider Man",
            FilmType.regular,
            FilmFormat.dvd,
            "Action",
            asList("english", "swedish", "spanish"),
            12,
            Date(2009, 6, 7),
            "Spider Man description")
    val spiderMan2Film = Film("3",
            "Spider Man 2",
            FilmType.regular,
            FilmFormat.dvd,
            "Action",
            asList("english", "swedish", "spanish"),
            12,
            Date(2012, 12, 3),
            "Spider Man 2 description")
    val outOfAfricaFilm = Film("4",
            "Out of Africa",
            FilmType.old,
            FilmFormat.vhs,
            "Drama",
            listOf("english"),
            18,
            Date(1990, 10, 23),
            "Out of Africa description")
}
