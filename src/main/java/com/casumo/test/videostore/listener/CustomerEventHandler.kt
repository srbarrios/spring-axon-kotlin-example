package com.casumo.test.videostore.listener

import com.casumo.test.videostore.coreapi.CustomerCreatedEvent
import com.casumo.test.videostore.coreapi.CustomerRemovedEvent
import com.casumo.test.videostore.coreapi.FilmsRentedEvent
import com.casumo.test.videostore.coreapi.FilmsReturnedEvent
import com.casumo.test.videostore.entity.Customer
import com.casumo.test.videostore.entity.Film
import com.casumo.test.videostore.entity.RentedFilm
import com.casumo.test.videostore.repository.CustomerRepository
import com.casumo.test.videostore.repository.FilmRepository
import com.casumo.test.videostore.repository.RentedFilmRepository
import com.casumo.test.videostore.utils.TimeProvider
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import java.time.temporal.ChronoUnit.DAYS
import java.util.*
import java.util.stream.Collectors

@Component
class CustomerEventHandler(private val filmRepository: FilmRepository,
                           private val customerRepository: CustomerRepository,
                           private val rentedFilmRepository: RentedFilmRepository,
                           private val timeProvider: TimeProvider) {

    @EventHandler
    protected fun on(event: CustomerCreatedEvent) {
        customerRepository.save(Customer(event.customerId, event.fullName, event.phoneNumber))
    }

    @EventHandler
    protected fun on(event: CustomerRemovedEvent) {
        customerRepository.delete(event.customerId)
    }

    @EventHandler
    protected fun on(event: FilmsRentedEvent) {
        addCustomerRentedFilms(event.customerId, event.films, event.days)
    }


    @EventHandler
    protected fun on(event: FilmsReturnedEvent) {
        removeCustomerRentedFilms(event.customerId, event.films)
    }


    private fun addCustomerRentedFilms(customerId: String, filmsIds: List<String>, days: Int) {
        val customer = customerRepository.findOne(customerId)
        val films = filmsIds
                .stream()
                .map<Film>({ filmRepository.findOne(it) })
                .collect(Collectors.toList())
        for (film in films) {
            val returnedFilm = customer.getRentedFilm(film)
            if (!returnedFilm.isPresent) {
                val now = timeProvider.currentDate
                val expectedReturnDate = Date.from(now.toInstant().plus(days.toLong(), DAYS))
                val newRentedFilm = RentedFilm(customer, film, now, expectedReturnDate)
                rentedFilmRepository.saveAndFlush(newRentedFilm)
                film.setRenter(customer)
                filmRepository.saveAndFlush(film)
                customer.addRentedFilm(newRentedFilm)
            }
        }
        customerRepository.saveAndFlush(customer)
    }

    private fun removeCustomerRentedFilms(customerId: String, filmsIds: List<String>) {
        val customer = customerRepository.findOne(customerId)
        val films = filmsIds
                .stream()
                .map<Film>({ filmRepository.findOne(it) })
                .collect(Collectors.toList())
        for (film in films) {
            val returnedFilm = customer.getRentedFilm(film)
            if (returnedFilm.isPresent) {
                val now = timeProvider.currentDate
                returnedFilm.get().returnDate = now
                rentedFilmRepository.saveAndFlush(returnedFilm.get())
                film.setRenter(null)
                filmRepository.saveAndFlush(film)
                customer.removeRentedFilm(returnedFilm.get())
            }
        }
        customerRepository.saveAndFlush(customer)
    }


}
