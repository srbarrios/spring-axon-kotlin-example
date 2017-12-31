package com.casumo.test.videostore.entity

import com.casumo.test.videostore.constants.FilmType
import com.casumo.test.videostore.constants.VideoStoreConstants.EXTRA_BONUS_POINTS
import com.casumo.test.videostore.constants.VideoStoreConstants.REGULAR_BONUS_POINTS
import com.casumo.test.videostore.controller.dto.CustomerDto
import java.util.*
import javax.persistence.*

@Entity
class Customer : CustomerDto {

    @Id
    lateinit var customerId: String

    var bonusPoints: Int = 0

    @Column
    @ElementCollection(targetClass = RentedFilm::class)
    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "customer")
    private var rentedFilmEntities: MutableList<RentedFilm> = mutableListOf()

    constructor(customerId: String, fullName: String, phoneNumber: Int) {
        this.customerId = customerId
        this.fullName = fullName
        this.phoneNumber = phoneNumber
        this.bonusPoints = bonusPoints
    }

    constructor() : super()

    fun getRentedFilmEntities(): List<RentedFilm> {
        return rentedFilmEntities
    }

    fun setRentedFilmEntities(rentedFilmEntities: MutableList<RentedFilm>) {
        this.rentedFilmEntities = rentedFilmEntities
    }

    fun addRentedFilm(newRentedFilm: RentedFilm) {
        this.rentedFilmEntities!!.add(newRentedFilm)
        when (newRentedFilm.film!!.type) {
            FilmType.newFilm -> this.bonusPoints += EXTRA_BONUS_POINTS
            else -> this.bonusPoints += REGULAR_BONUS_POINTS
        }
    }

    fun removeRentedFilm(newRentedFilm: RentedFilm) {
        this.rentedFilmEntities!!.remove(newRentedFilm)
    }

    fun getRentedFilm(film: Film): Optional<RentedFilm> {
        return this.rentedFilmEntities!!
                .stream()
                .filter { rentedFilm -> rentedFilm.film == film }
                .findFirst()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Customer

        if (customerId != other.customerId) return false
        if (bonusPoints != other.bonusPoints) return false
        if (rentedFilmEntities != other.rentedFilmEntities) return false

        return true
    }

    override fun hashCode(): Int {
        var result = customerId.hashCode()
        result = 31 * result + bonusPoints
        result = 31 * result + rentedFilmEntities.hashCode()
        return result
    }


}
