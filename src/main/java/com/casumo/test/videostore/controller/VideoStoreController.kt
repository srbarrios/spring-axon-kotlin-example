package com.casumo.test.videostore.controller

import com.casumo.test.videostore.constants.FilmType
import com.casumo.test.videostore.constants.VideoStoreConstants.BASIC_PRICE
import com.casumo.test.videostore.constants.VideoStoreConstants.PENALTY_PRICE
import com.casumo.test.videostore.constants.VideoStoreConstants.PREMIUM_PRICE
import com.casumo.test.videostore.controller.dto.RentFilmsDto
import com.casumo.test.videostore.controller.dto.ReturnFilmsDto
import com.casumo.test.videostore.coreapi.RentFilmsCommand
import com.casumo.test.videostore.coreapi.ReturnFilmsCommand
import com.casumo.test.videostore.entity.Film
import com.casumo.test.videostore.entity.RentedFilm
import com.casumo.test.videostore.repository.CustomerRepository
import com.casumo.test.videostore.repository.FilmRepository
import com.casumo.test.videostore.repository.RentedFilmRepository
import com.casumo.test.videostore.utils.TimeProvider
import org.axonframework.commandhandling.gateway.CommandGateway
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

@RestController
class VideoStoreController(private val filmRepository: FilmRepository,
                           private val customerRepository: CustomerRepository,
                           private val rentedFilmRepository: RentedFilmRepository,
                           private val timeProvider: TimeProvider,
                           private val commandGateway: CommandGateway) {

    private val logger = LoggerFactory.getLogger(VideoStoreController::class.java)

    @GetMapping("/videostore/rentedfilms")
    @ResponseBody
    fun rentedFilms(): ResponseEntity<List<RentedFilm>> {
        val rentedFilms = rentedFilmRepository.findAll()
        return ResponseEntity(rentedFilms, HttpStatus.OK)

    }

    @PostMapping("/videostore/rent")
    @ResponseBody
    fun rentFilms(@RequestBody rentFilmsDto: RentFilmsDto): ResponseEntity<Long> {
        val rentedFilms = rentedFilmRepository.findAll()
        val alreadyRentedFilms = rentedFilms
                .stream()
                .filter { rentedFilm -> rentFilmsDto.filmsIds!!.contains(rentedFilm.film!!.filmId) }
                .collect(Collectors.toList())
        if (alreadyRentedFilms.isEmpty()) {
            commandGateway.send<Any>(RentFilmsCommand(rentFilmsDto.customerId, rentFilmsDto.filmsIds!!, rentFilmsDto.days))
            val rentalPrice = calculateRentalPrice(rentFilmsDto.filmsIds, rentFilmsDto.days)
            return ResponseEntity(rentalPrice, HttpStatus.OK)
        } else {
            return ResponseEntity(0L, HttpStatus.CONFLICT)
        }
    }

    @PostMapping("/videostore/return")
    @ResponseBody
    fun returnFilms(@RequestBody returnFilmsDto: ReturnFilmsDto): ResponseEntity<Long> {
        commandGateway.send<Any>(ReturnFilmsCommand(returnFilmsDto.customerId, returnFilmsDto.filmsIds!!))
        val surcharge = calculateSurcharge(returnFilmsDto.customerId, returnFilmsDto.filmsIds)
        return ResponseEntity(surcharge, HttpStatus.OK)
    }

    private fun calculateSurcharge(customerId: String, filmsIds: List<String>?): Long {
        val customer = customerRepository.findOne(customerId)
        val films = filmsIds!!
                .stream()
                .map<Film>({ filmRepository.findOne(it) })
                .collect(Collectors.toList())
        var surcharge: Long = 0
        for (film in films) {
            val returnedFilm = customer.getRentedFilm(film)
            if (returnedFilm.isPresent) {
                val returnDate = timeProvider.currentDate.time //Not the exact time stored but enough to compare days
                val expectedDate = returnedFilm.get().expectedReturnDate!!.time
                val exceededDays = TimeUnit.MILLISECONDS.toDays(returnDate - expectedDate)
                if (exceededDays > 0) {
                    surcharge += exceededDays * PENALTY_PRICE
                }
            }
        }
        return surcharge
    }

    private fun calculateRentalPrice(filmsIds: List<String>?, days: Int): Long {
        val films = filmsIds!!
                .stream()
                .map<Film>({ filmRepository.findOne(it) })
                .collect(Collectors.toList())
        var rentalPrice: Long = 0
        for (film in films) {
            when (film.type) {
                FilmType.newFilm -> rentalPrice += (days * PREMIUM_PRICE).toLong()
                FilmType.regular -> rentalPrice += (BASIC_PRICE + Math.max(0, days - 3) * BASIC_PRICE).toLong()
                FilmType.old -> rentalPrice += (BASIC_PRICE + Math.max(0, days - 5) * BASIC_PRICE).toLong()
                else -> logger.warn("Film type " + film.type.toString() + " don't have rental price")
            }
        }
        return rentalPrice
    }
}
