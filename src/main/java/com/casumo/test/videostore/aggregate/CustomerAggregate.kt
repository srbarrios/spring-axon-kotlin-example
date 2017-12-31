package com.casumo.test.videostore.aggregate

import com.casumo.test.videostore.coreapi.*
import com.casumo.test.videostore.entity.RentedFilm
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class CustomerAggregate {

    @AggregateIdentifier
    private var customerId: String? = null
    private val fullName: String? = null
    private val phoneNumber: Int? = null
    private val bonusPoints: Int? = null
    private val rentedFilmEntities: List<RentedFilm>? = null

    constructor() {}

    @CommandHandler
    constructor(cmd: CreateCustomerCommand) {
        apply(CustomerCreatedEvent(cmd.customerId, cmd.fullName, cmd.phoneNumber))
    }

    @EventSourcingHandler
    protected fun on(event: CustomerCreatedEvent) {
        this.customerId = event.customerId
    }

    @CommandHandler
    fun handle(cmd: RemoveCustomerCommand) {
        apply(CustomerRemovedEvent(cmd.customerId))
    }

    @CommandHandler
    fun handle(cmd: RentFilmsCommand) {
        apply(FilmsRentedEvent(cmd.customerId, cmd.films, cmd.days))
    }

    @CommandHandler
    fun handle(cmd: ReturnFilmsCommand) {
        apply(FilmsReturnedEvent(cmd.customerId, cmd.films))
    }

}
