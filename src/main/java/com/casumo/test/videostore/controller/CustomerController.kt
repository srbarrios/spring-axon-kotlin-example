package com.casumo.test.videostore.controller

import com.casumo.test.videostore.controller.dto.CustomerDto
import com.casumo.test.videostore.coreapi.CreateCustomerCommand
import com.casumo.test.videostore.coreapi.RemoveCustomerCommand
import com.casumo.test.videostore.entity.Customer
import com.casumo.test.videostore.repository.CustomerRepository
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

@RestController
class CustomerController(private val commandGateway: CommandGateway, private val repository: CustomerRepository) {

    val customers: ResponseEntity<List<Customer>>
        @GetMapping("/customers")
        @ResponseBody
        get() {
            val customers = repository.findAll()
            return ResponseEntity(customers, HttpStatus.OK)
        }

    @GetMapping("/customer/{customerId}")
    @ResponseBody
    fun getCustomer(@PathVariable customerId: String): ResponseEntity<Customer> {
        val customer = repository.findOne(customerId)
        return if (customer != null) {
            ResponseEntity(customer, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/customer/films/{customerId}")
    @ResponseBody
    fun getRentedFilms(@PathVariable customerId: String): ResponseEntity<List<String>> {
        val customer = repository.findOne(customerId)
        val films = customer
                .getRentedFilmEntities()
                .stream()
                .map { rentedFilm -> rentedFilm.film!!.filmId }
                .collect(Collectors.toList())
        return ResponseEntity(films, HttpStatus.OK)
    }


    @PostMapping("/customer")
    @ResponseBody
    fun createCustomer(@RequestBody customer: CustomerDto): ResponseEntity<String> {
        val customerId = UUID.randomUUID().toString()
        commandGateway.send(
                CreateCustomerCommand(
                        customerId,
                        customer.fullName,
                        customer.phoneNumber
                ),
                LoggingCallback.INSTANCE)
        return ResponseEntity(customerId, HttpStatus.CREATED)
    }

    @DeleteMapping("/customer/{customerId}")
    @ResponseBody
    fun deleteCustomer(@PathVariable customerId: String): ResponseEntity<String> {
        commandGateway.send(RemoveCustomerCommand(customerId), LoggingCallback.INSTANCE)
        return ResponseEntity(customerId, HttpStatus.NO_CONTENT)
    }
}
