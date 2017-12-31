package com.casumo.test.videostore.controller.dto

class RentFilmsDto {

    lateinit var customerId: String

    var filmsIds: List<String>? = null

    var days: Int = 0

    constructor() {}

    constructor(customerId: String, filmsIds: List<String>, days: Int) {
        this.customerId = customerId
        this.filmsIds = filmsIds
        this.days = days
    }
}
