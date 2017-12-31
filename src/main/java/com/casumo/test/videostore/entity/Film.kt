package com.casumo.test.videostore.entity

import com.casumo.test.videostore.constants.FilmFormat
import com.casumo.test.videostore.constants.FilmType
import com.casumo.test.videostore.controller.dto.FilmDto
import com.casumo.test.videostore.coreapi.CreateFilmCommand
import com.casumo.test.videostore.coreapi.FilmCreatedEvent
import com.casumo.test.videostore.coreapi.FilmRemovedEvent
import com.casumo.test.videostore.coreapi.RemoveFilmCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Film : FilmDto {

    @Id
    lateinit var filmId: String

    @ManyToOne(cascade = arrayOf(CascadeType.DETACH), targetEntity = Customer::class)
    private var renter: Customer? = null

    constructor() {}

    constructor(filmId: String, title: String, type: FilmType, format: FilmFormat, genre: String, languages: List<String>, minimumAge: Int?,
                releaseDate: Date, description: String) {
        this.filmId = filmId
        this.title = title
        this.type = type
        this.format = format
        this.genre = genre
        this.languages = languages
        this.minimumAge = minimumAge!!
        this.releaseDate = releaseDate
        this.description = description
        this.renter = null
    }

    fun getRenter(): Optional<Customer> {
        return Optional.ofNullable(renter)
    }

    fun setRenter(renter: Customer?) {
        this.renter = renter
    }

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
    constructor(cmd: RemoveFilmCommand) {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Film

        if (filmId != other.filmId) return false
        if (renter != other.renter) return false

        return true
    }

    override fun hashCode(): Int {
        var result = filmId.hashCode()
        result = 31 * result + (renter?.hashCode() ?: 0)
        return result
    }


}
