package com.casumo.test.videostore.controller

import com.casumo.test.videostore.controller.dto.FilmDto
import com.casumo.test.videostore.coreapi.CreateFilmCommand
import com.casumo.test.videostore.coreapi.RemoveFilmCommand
import com.casumo.test.videostore.entity.Film
import com.casumo.test.videostore.repository.FilmRepository
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class FilmController(private val commandGateway: CommandGateway, private val repository: FilmRepository) {

    val films: ResponseEntity<List<Film>>
        @GetMapping("/films")
        @ResponseBody
        get() {
            val films = repository.findAll()
            return ResponseEntity(films, HttpStatus.OK)
        }

    @GetMapping("/film/{filmId}")
    @ResponseBody
    fun getFilm(@PathVariable filmId: String): ResponseEntity<Film> {
        val film = repository.findOne(filmId)
        return if (film != null) {
            ResponseEntity(film, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/film")
    @ResponseBody
    fun createFilm(@RequestBody film: FilmDto): ResponseEntity<String> {
        val filmId = UUID.randomUUID().toString()
        commandGateway.send(
                CreateFilmCommand(
                        filmId,
                        film.title,
                        film.type,
                        film.format,
                        film.genre,
                        film.languages,
                        film.minimumAge,
                        film.releaseDate,
                        film.description
                ),
                LoggingCallback.INSTANCE)
        return ResponseEntity(filmId, HttpStatus.CREATED)
    }

    @DeleteMapping("/film/{filmId}")
    @ResponseBody
    fun deleteFilm(@PathVariable filmId: String): ResponseEntity<String> {
        commandGateway.send(RemoveFilmCommand(filmId), LoggingCallback.INSTANCE)
        return ResponseEntity(filmId, HttpStatus.NO_CONTENT)
    }


}
