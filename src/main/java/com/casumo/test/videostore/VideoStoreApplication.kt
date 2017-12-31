package com.casumo.test.videostore

import org.axonframework.config.EventHandlingConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class VideoStoreApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(
                    VideoStoreApplication::class.java, *args)
        }
    }

    @Autowired
    fun configure(config: EventHandlingConfiguration) {
        config.registerTrackingProcessor("com.casumo.test.videostore.listener")
    }

}