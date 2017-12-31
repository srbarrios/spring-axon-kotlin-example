package com.casumo.test.videostore.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
class RentedFilm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val rentedFilmId: Long? = null

    @JsonIgnoreProperties("rentedFilmEntities")
    @ManyToOne(cascade = arrayOf(CascadeType.PERSIST), targetEntity = Customer::class)
    var customer: Customer? = null

    @JsonIgnoreProperties("renter")
    @ManyToOne(cascade = arrayOf(CascadeType.PERSIST), targetEntity = Film::class)
    var film: Film? = null


    var rentedDate: Date? = null

    var expectedReturnDate: Date? = null

    var returnDate: Date? = null

    constructor() {}

    constructor(customer: Customer, film: Film, rentedDate: Date, expectedReturnDate: Date) {
        this.customer = customer
        this.film = film
        this.rentedDate = rentedDate
        this.expectedReturnDate = expectedReturnDate
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RentedFilm

        if (rentedFilmId != other.rentedFilmId) return false
        if (customer != other.customer) return false
        if (film != other.film) return false
        if (rentedDate != other.rentedDate) return false
        if (expectedReturnDate != other.expectedReturnDate) return false
        if (returnDate != other.returnDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rentedFilmId?.hashCode() ?: 0
        result = 31 * result + (customer?.hashCode() ?: 0)
        result = 31 * result + (film?.hashCode() ?: 0)
        result = 31 * result + (rentedDate?.hashCode() ?: 0)
        result = 31 * result + (expectedReturnDate?.hashCode() ?: 0)
        result = 31 * result + (returnDate?.hashCode() ?: 0)
        return result
    }


}
