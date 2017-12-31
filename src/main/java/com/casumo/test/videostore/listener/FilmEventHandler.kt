package com.casumo.test.videostore.listener

import com.casumo.test.videostore.coreapi.FilmCreatedEvent
import com.casumo.test.videostore.coreapi.FilmRemovedEvent
import com.casumo.test.videostore.entity.Film
import com.casumo.test.videostore.repository.FilmRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.web.bind.annotation.RestController

@RestController
class FilmEventHandler(private val repository: FilmRepository) {

    @EventHandler
    protected fun on(event: FilmCreatedEvent) {
        repository.save(Film(event.filmId,
                event.title,
                event.type,
                event.format,
                event.genre,
                event.languages,
                event.minimumAge,
                event.releaseDate,
                event.description))
    }

    @EventHandler
    protected fun on(event: FilmRemovedEvent) {
        repository.delete(event.filmId)
    }
}
