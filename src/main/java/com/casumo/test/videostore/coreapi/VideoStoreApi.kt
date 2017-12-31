package com.casumo.test.videostore.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

class RentFilmsCommand(@TargetAggregateIdentifier val customerId: String, val films: List<String>, val days: Int)

class FilmsRentedEvent(val customerId: String, val films: List<String>, val days: Int)

class ReturnFilmsCommand(@TargetAggregateIdentifier val customerId: String, val films: List<String>)

class FilmsReturnedEvent(val customerId: String, val films: List<String>)
