package com.casumo.test.videostore.rest

import com.casumo.test.videostore.VideoStoreApplication
import com.casumo.test.videostore.constants.VideoStoreConstants.SECURITY_TOKEN_TESTING
import com.casumo.test.videostore.controller.dto.CustomerDto
import com.casumo.test.videostore.controller.dto.FilmDto
import com.casumo.test.videostore.controller.dto.RentFilmsDto
import com.casumo.test.videostore.controller.dto.ReturnFilmsDto
import com.casumo.test.videostore.entity.Customer
import com.casumo.test.videostore.entity.Film
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONException
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.io.IOException
import java.lang.Thread.sleep


@RunWith(SpringRunner::class)
@ActiveProfiles("test")
@SpringBootTest(classes = arrayOf(VideoStoreApplication::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiRestTest {

    @LocalServerPort
    private val port: Int = 0

    internal var restTemplate = TestRestTemplate()

    internal var headers = HttpHeaders()

    private var customerId: String? = null

    private var matrixFilmId: String? = null

    private var spiderManFilmId: String? = null

    private var spiderMan2FilmId: String? = null

    private var outOfAfricaFilmId: String? = null


    @Test
    @Throws(JSONException::class)
    fun testRetrieveCustomers() {
        val entity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(createURLWithPort("/customers"), HttpMethod.GET, entity, String::class.java)

        try {
            ObjectMapper().readValue<Any>(response.body, object : TypeReference<List<Customer>>() {

            })
        } catch (e: IOException) {
            throw AssertionError("Can't read the response of /customers endpoint")
        }

    }

    @Test
    @Throws(JSONException::class)
    fun testRetrieveFilms() {
        val entity = HttpEntity<String>(null, headers)
        val response = restTemplate.exchange(createURLWithPort("/films"), HttpMethod.GET, entity, String::class.java)

        val mapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        try {
            val films = mapper.readValue<List<FilmDto>>(response.body, object : TypeReference<List<FilmDto>>() {

            })
            films.forEach { filmDto -> Assert.assertFalse(filmDto.title.isEmpty()) }
        } catch (e: IOException) {
            throw AssertionError("Can't read the response of /films endpoint")
        }

    }

    @Test
    @Throws(InterruptedException::class)
    fun testACustomerRentAFilm() {
        setUpForRent()

        val entity = HttpEntity(RentFilmsDto(customerId!!, listOf<String>(matrixFilmId!!), 1), headers)
        val response = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity, String::class.java)
        assertEquals("40", response.body)

        val entity2 = HttpEntity(RentFilmsDto(customerId!!, listOf<String>(spiderManFilmId!!), 5), headers)
        val response2 = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity2, String::class.java)
        assertEquals("90", response2.body)

        val entity3 = HttpEntity(RentFilmsDto(customerId!!, listOf<String>(spiderMan2FilmId!!), 2), headers)
        val response3 = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity3, String::class.java)
        assertEquals("30", response3.body)

        val entity4 = HttpEntity(RentFilmsDto(customerId!!, listOf<String>(outOfAfricaFilmId!!), 7), headers)
        val response4 = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity4, String::class.java)
        assertEquals("90", response4.body)
    }

    @Test
    @Throws(InterruptedException::class)
    fun testTwoCustomersRentSameFilmAtSameTime() {
        setUpForRent()

        //Create Customer 2
        val entity1 = HttpEntity(CustomerDto("Second Customer", 222222222), headers)
        val response1 = restTemplate.exchange(createURLWithPort("/customer"), HttpMethod.POST, entity1, String::class.java)
        val customer2Id = response1.body

        val entity = HttpEntity(RentFilmsDto(customerId!!, listOf<String>(matrixFilmId!!), 1), headers)
        val response = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity, String::class.java)
        assertEquals("40", response.body)

        sleep(100) //TODO: See how we can improve it to know when the data is actually stored

        val entity2 = HttpEntity(RentFilmsDto(customer2Id, listOf<String>(matrixFilmId!!), 1), headers)
        val response2 = restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity2, String::class.java)
        assertEquals("0", response2.body)
        assertEquals(HttpStatus.CONFLICT, response2.statusCode)
    }

    @Test
    @Throws(InterruptedException::class)
    fun testACustomerReturnAFilm() {
        setUpForRent()

        restTemplate.exchange(createURLWithPort("/testutils/fixdate/2017-01-01/" + SECURITY_TOKEN_TESTING), HttpMethod.POST, HttpEntity<Any>(headers), String::class.java)

        val entity2 = HttpEntity(RentFilmsDto(customerId!!, listOf<String>(matrixFilmId!!), 1), headers)
        restTemplate.exchange(createURLWithPort("/videostore/rent"), HttpMethod.POST, entity2, String::class.java)
        sleep(100) //TODO: See how we can improve it to know when the data is actually stored

        restTemplate.exchange(createURLWithPort("/testutils/fixdate/2017-01-04/" + SECURITY_TOKEN_TESTING), HttpMethod.POST, HttpEntity<Any>(headers), String::class.java)

        val entity4 = HttpEntity(ReturnFilmsDto(customerId!!, listOf<String>(matrixFilmId!!)), headers)
        val response = restTemplate.exchange(createURLWithPort("/videostore/return"), HttpMethod.POST, entity4, String::class.java)
        assertEquals("80", response.body)

    }

    @Throws(InterruptedException::class)
    private fun setUpForRent() {
        //Create Customer
        val entity1 = HttpEntity(CustomerDto("John Carter", 123469587), headers)
        val response1 = restTemplate.exchange(createURLWithPort("/customer"), HttpMethod.POST, entity1, String::class.java)
        customerId = response1.body

        //Create films
        val response2 = restTemplate.exchange(createURLWithPort("/film"), HttpMethod.POST, HttpEntity<Film>(matrixFilm, headers), String::class.java)
        matrixFilmId = response2.body

        val response3 = restTemplate.exchange(createURLWithPort("/film"), HttpMethod.POST, HttpEntity<Film>(spiderManFilm, headers), String::class.java)
        spiderManFilmId = response3.body

        val response4 = restTemplate.exchange(createURLWithPort("/film"), HttpMethod.POST, HttpEntity<Film>(spiderMan2Film, headers), String::class.java)
        spiderMan2FilmId = response4.body

        val response5 = restTemplate.exchange(createURLWithPort("/film"), HttpMethod.POST, HttpEntity<Film>(outOfAfricaFilm, headers), String::class.java)
        outOfAfricaFilmId = response5.body

        sleep(100)
    }

    private fun createURLWithPort(uri: String): String {
        return "http://localhost:" + port + uri
    }

    companion object {

        private val logger = LoggerFactory.getLogger(ApiRestTest::class.java)
    }
}