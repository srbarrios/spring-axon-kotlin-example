package com.casumo.test.videostore.controller.dto

import com.casumo.test.videostore.constants.FilmFormat
import com.casumo.test.videostore.constants.FilmType
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class FilmDto {

    lateinit var title: String

    lateinit var type: FilmType

    lateinit var format: FilmFormat

    lateinit var genre: String

    @ElementCollection(targetClass = String::class)
    lateinit var languages: List<String>

    var minimumAge: Int = 0

    lateinit var releaseDate: Date

    lateinit var description: String

    constructor() {}

    constructor(title: String, type: FilmType, format: FilmFormat, genre: String, languages: List<String>, minimumAge: Int, releaseDate: Date, description: String) {
        this.title = title
        this.type = type
        this.format = format
        this.genre = genre
        this.languages = languages
        this.minimumAge = minimumAge
        this.releaseDate = releaseDate
        this.description = description
    }
}
