package com.casumo.test.videostore.callback

import org.axonframework.commandhandling.CommandCallback
import org.axonframework.commandhandling.CommandMessage
import org.slf4j.LoggerFactory
import java.util.*

class LoggingCallback<C, R> private constructor(creator: (C) -> R) : CommandCallback<C, R> {

    private val logger = LoggerFactory.getLogger(LoggingCallback::class.java)

    override fun onSuccess(commandMessage: CommandMessage<out C>, result: R) {
        logger.info("Command successful: {} -> {}", commandMessage.payloadType.simpleName, Objects.toString(result))
    }

    override fun onFailure(commandMessage: CommandMessage<out C>, cause: Throwable) {
        cause.printStackTrace()
        logger.info("Command failed: {} -> {}", commandMessage.payloadType.simpleName,
                cause.javaClass.simpleName)
    }

}
