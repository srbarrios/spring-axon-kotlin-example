package com.casumo.test.videostore.aggregate

import com.casumo.test.videostore.constants.FilmFormat
import com.casumo.test.videostore.constants.FilmType
import com.casumo.test.videostore.coreapi.CreateFilmCommand
import com.casumo.test.videostore.coreapi.FilmCreatedEvent
import com.casumo.test.videostore.coreapi.FilmRemovedEvent
import com.casumo.test.videostore.coreapi.RemoveFilmCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.util.*

@Aggregate
class FilmAggregate {

    @AggregateIdentifier
    private var filmId: String? = null
    private var title: String? = null
    private var type: FilmType? = null
    private var format: FilmFormat? = null
    private var genre: String? = null
    private var languages: List<String>? = null
    private var minimumAge: Int? = null
    private var releaseDate: Date? = null
    private var description: String? = null
    private val renter: CustomerAggregate? = null

    constructor() {}

    @CommandHandler
    constructor(cmd: CreateFilmCommand) {
        apply(FilmCreatedEvent(
                cmd.filmId,
                cmd.title,
                cmd.type,
                cmd.format,
                cmd.genre,
                cmd.languages,
                cmd.minimumAge,
                cmd.releaseDate,
                cmd.description
        ))
    }

    @CommandHandler
    fun handle(cmd: RemoveFilmCommand) {
        apply(FilmRemovedEvent(
                cmd.filmId
        ))
    }

    @EventSourcingHandler
    protected fun on(event: FilmCreatedEvent) {
        this.filmId = event.filmId
        this.type = event.type
        this.title = event.title
        this.format = event.format
        this.genre = event.genre
        this.languages = event.languages
        this.minimumAge = event.minimumAge
        this.description = event.description
        this.releaseDate = event.releaseDate
    }

}
