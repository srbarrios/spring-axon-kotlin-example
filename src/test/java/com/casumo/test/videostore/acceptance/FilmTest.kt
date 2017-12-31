package com.casumo.test.videostore.acceptance

import com.casumo.test.videostore.acceptance.VideoStoreTestConstants.matrixFilm
import com.casumo.test.videostore.aggregate.FilmAggregate
import com.casumo.test.videostore.coreapi.CreateFilmCommand
import com.casumo.test.videostore.coreapi.FilmCreatedEvent
import com.casumo.test.videostore.coreapi.FilmRemovedEvent
import com.casumo.test.videostore.coreapi.RemoveFilmCommand
import org.axonframework.eventsourcing.eventstore.EventStoreException
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test

class FilmTest {

    private var fixture: FixtureConfiguration<FilmAggregate>? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        fixture = AggregateTestFixture(FilmAggregate::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testFilmCreated() {
        fixture!!.givenNoPriorActivity()
                .`when`(CreateFilmCommand(
                        matrixFilm.filmId,
                        matrixFilm.title,
                        matrixFilm.type,
                        matrixFilm.format,
                        matrixFilm.genre,
                        matrixFilm.languages,
                        matrixFilm.minimumAge,
                        matrixFilm.releaseDate,
                        matrixFilm.description))
                .expectEvents(FilmCreatedEvent(
                        matrixFilm.filmId,
                        matrixFilm.title,
                        matrixFilm.type,
                        matrixFilm.format,
                        matrixFilm.genre,
                        matrixFilm.languages,
                        matrixFilm.minimumAge,
                        matrixFilm.releaseDate,
                        matrixFilm.description))
    }

    @Test
    @Throws(Exception::class)
    fun testUserCantCreateSameFilmTwice() {
        fixture!!.given(FilmCreatedEvent(
                matrixFilm.filmId,
                matrixFilm.title,
                matrixFilm.type,
                matrixFilm.format,
                matrixFilm.genre,
                matrixFilm.languages,
                matrixFilm.minimumAge,
                matrixFilm.releaseDate,
                matrixFilm.description))
                .`when`(CreateFilmCommand(
                        matrixFilm.filmId,
                        matrixFilm.title,
                        matrixFilm.type,
                        matrixFilm.format,
                        matrixFilm.genre,
                        matrixFilm.languages,
                        matrixFilm.minimumAge,
                        matrixFilm.releaseDate,
                        matrixFilm.description))
                .expectEvents(FilmCreatedEvent(
                        matrixFilm.filmId,
                        matrixFilm.title,
                        matrixFilm.type,
                        matrixFilm.format,
                        matrixFilm.genre,
                        matrixFilm.languages,
                        matrixFilm.minimumAge,
                        matrixFilm.releaseDate,
                        matrixFilm.description))
                .expectException(EventStoreException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testFilmRemoved() {
        fixture!!.givenCommands(CreateFilmCommand(
                matrixFilm.filmId,
                matrixFilm.title,
                matrixFilm.type,
                matrixFilm.format,
                matrixFilm.genre,
                matrixFilm.languages,
                matrixFilm.minimumAge,
                matrixFilm.releaseDate,
                matrixFilm.description))
                .`when`(RemoveFilmCommand(
                        matrixFilm.filmId))
                .expectEvents(FilmRemovedEvent(
                        matrixFilm.filmId))
    }

}
