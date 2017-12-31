package com.casumo.test.videostore.listener

import org.axonframework.eventhandling.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoggingEventHandler {

    private val logger = LoggerFactory.getLogger(LoggingEventHandler::class.java)

    @EventHandler
    fun on(event: Any) {
        logger.debug("Event received: " + event.toString())
    }
}
