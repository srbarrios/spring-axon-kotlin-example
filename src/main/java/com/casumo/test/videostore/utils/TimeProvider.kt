package com.casumo.test.videostore.utils

import java.util.*

interface TimeProvider {

    val currentDate: Date

    fun setFixedDate(date: Date)
}