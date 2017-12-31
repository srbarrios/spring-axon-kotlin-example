package com.casumo.test.videostore.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

class CreateCustomerCommand(@TargetAggregateIdentifier val customerId: String, val fullName: String, val phoneNumber: Int)

class CustomerCreatedEvent(val customerId: String, val fullName: String, val phoneNumber: Int)

class RemoveCustomerCommand(@TargetAggregateIdentifier val customerId: String)

class CustomerRemovedEvent(val customerId: String)