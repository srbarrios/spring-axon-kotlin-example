package com.casumo.test.videostore.controller.dto

import javax.persistence.MappedSuperclass

@MappedSuperclass
open class CustomerDto {

    lateinit var fullName: String

    var phoneNumber: Int = 0

    constructor() {}

    constructor(fullName: String, phoneNumber: Int) {
        this.fullName = fullName
        this.phoneNumber = phoneNumber
    }

}
