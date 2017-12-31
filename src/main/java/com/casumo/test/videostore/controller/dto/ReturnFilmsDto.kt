package com.casumo.test.videostore.controller.dto

class ReturnFilmsDto {

    lateinit var customerId: String

    var filmsIds: List<String>? = null

    constructor() {}

    constructor(customerId: String, filmsIds: List<String>) {
        this.customerId = customerId
        this.filmsIds = filmsIds
    }
}
