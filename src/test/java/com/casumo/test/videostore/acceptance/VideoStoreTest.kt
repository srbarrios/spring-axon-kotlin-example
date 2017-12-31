package com.casumo.test.videostore.acceptance

import com.casumo.test.videostore.aggregate.CustomerAggregate
import com.casumo.test.videostore.constants.FilmFormat
import com.casumo.test.videostore.constants.FilmType
import com.casumo.test.videostore.coreapi.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.util.*

class VideoStoreTest {

    private var fixture: FixtureConfiguration<CustomerAggregate>? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        fixture = AggregateTestFixture(CustomerAggregate::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testRentAFilm() {
        fixture!!.given(CustomerCreatedEvent("customer1", "name1", 0),
                FilmCreatedEvent("film1", "GhostBusters",
                        FilmType.old,
                        FilmFormat.vhs,
                        "SciFy",
                        listOf("english"),
                        12,
                        Date(1995, 1, 1),
                        ""))
                .`when`(RentFilmsCommand("customer1", listOf("film1"), 3))
                .expectEvents(FilmsRentedEvent("customer1", listOf("film1"), 3))
    }

    @Test
    fun testReturnAFilm() {
        fixture!!.given(CustomerCreatedEvent("customer1", "name1", 0),
                FilmCreatedEvent("film1", "GhostBusters",
                        FilmType.old,
                        FilmFormat.vhs,
                        "SciFy",
                        listOf("english"),
                        12,
                        Date(1995, 1, 1),
                        ""),
                FilmsRentedEvent("customer1", listOf("film1"), 3))
                .`when`(ReturnFilmsCommand("customer1", listOf("film1")))
                .expectEvents(FilmsReturnedEvent("customer1", listOf("film1")))
    }
}
