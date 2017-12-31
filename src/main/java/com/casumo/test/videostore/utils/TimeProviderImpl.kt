package com.casumo.test.videostore.utils

import org.springframework.stereotype.Component
import java.util.*

@Component
class TimeProviderImpl : TimeProvider {

    internal var fixedDate: Date? = null

    override val currentDate: Date
        get() = if (fixedDate == null) Date() else fixedDate!!

    override fun setFixedDate(date: Date) {
        this.fixedDate = date
    }

}
