package com.casumo.test.videostore.coreapi

import com.casumo.test.videostore.constants.FilmFormat
import com.casumo.test.videostore.constants.FilmType
import org.axonframework.commandhandling.TargetAggregateIdentifier
import java.util.*

class CreateFilmCommand(@TargetAggregateIdentifier val filmId: String, val title: String, val type: FilmType,
                        val format: FilmFormat, val genre: String, val languages: List<String>,
                        val minimumAge: Int, val releaseDate: Date, val description: String)

class FilmCreatedEvent(val filmId: String, val title: String, val type: FilmType, val format: FilmFormat,
                       val genre: String, val languages: List<String>, val minimumAge: Int,
                       val releaseDate: Date, val description: String)

class RemoveFilmCommand(@TargetAggregateIdentifier val filmId: String)

class FilmRemovedEvent(val filmId: String)
