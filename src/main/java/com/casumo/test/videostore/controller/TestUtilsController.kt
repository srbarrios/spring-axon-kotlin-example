package com.casumo.test.videostore.controller

import com.casumo.test.videostore.constants.VideoStoreConstants.SECURITY_TOKEN_TESTING
import com.casumo.test.videostore.utils.TimeProvider
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TestUtilsController(private val timeProvider: TimeProvider) {

    @PostMapping("/testutils/fixdate/{date}/{securitytoken}")
    @ResponseBody
    fun fixDate(@PathVariable @DateTimeFormat(iso = DATE) date: Date, @PathVariable securitytoken: String): ResponseEntity<Date> {
        if (securitytoken != SECURITY_TOKEN_TESTING) return ResponseEntity(date, HttpStatus.FORBIDDEN)
        timeProvider.setFixedDate(date)
        return ResponseEntity(date, HttpStatus.OK)
    }
}
