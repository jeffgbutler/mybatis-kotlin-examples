package example08

import java.time.LocalDate

/*
 * In this version of the model, all classes are fully immutable. With MyBatis 3.6 and later, we can make these classes
 * immutable because starting with that version, MyBatis is able to build nested properties before calling the outer
 * constructor.
 *
 * We still need to use XML for defining the result maps - this is because the MyBatis annotations cannot be used to
 * describe nested collections.
 */

data class AddressWithPeople(
    val id: Int,
    val streetAddress: String,
    val city: String,
    val state: String,
    val people: List<Person>
)

data class Person(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val employed: Boolean,
    val occupation: String?
)

data class Address(
    val id: Int,
    val streetAddress: String,
    val city: String,
    val state: String
)

data class PersonWithAddress(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val employed: Boolean,
    val occupation: String?,
    val address: Address?
)
