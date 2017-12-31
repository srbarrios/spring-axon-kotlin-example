package com.casumo.test.videostore.acceptance

import com.casumo.test.videostore.aggregate.CustomerAggregate
import com.casumo.test.videostore.coreapi.CreateCustomerCommand
import com.casumo.test.videostore.coreapi.CustomerCreatedEvent
import com.casumo.test.videostore.coreapi.CustomerRemovedEvent
import com.casumo.test.videostore.coreapi.RemoveCustomerCommand
import org.axonframework.eventsourcing.eventstore.EventStoreException
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test

class CustomerTest {

    private var fixture: FixtureConfiguration<CustomerAggregate>? = null


    @Before
    @Throws(Exception::class)
    fun setUp() {
        fixture = AggregateTestFixture(CustomerAggregate::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testCustomerCreated() {
        fixture!!.givenNoPriorActivity()
                .`when`(CreateCustomerCommand("customer1", "name1", 0))
                .expectEvents(CustomerCreatedEvent("customer1", "name1", 0))
    }

    @Test
    @Throws(Exception::class)
    fun testSameCustomerIdCantBeCreatedTwice() {
        fixture!!.given(CustomerCreatedEvent("customer1", "name1", 0))
                .`when`(CreateCustomerCommand("customer1", "name1", 0))
                .expectException(EventStoreException::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testCustomerRemoved() {
        fixture!!.givenCommands(CreateCustomerCommand("customer1", "name1", 0))
                .`when`(RemoveCustomerCommand(
                        "customer1"))
                .expectEvents(CustomerRemovedEvent(
                        "customer1"))
    }


}
