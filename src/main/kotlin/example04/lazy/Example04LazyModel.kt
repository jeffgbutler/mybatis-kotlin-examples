package example04.lazy

import java.time.LocalDate

/*
 * In this version of the model, we have an AddressWithPeople class that includes a nested list of the Persons with the
 * address. This shows how to code a lazy loaded association.
 *
 * In this version, we expect the nested list to be populated by a second query, so we can declare it
 * as a "lateinit var" like the other properties.
 *
 * Important notes: when enabling lazy loading, MyBatis uses Javassist to create proxies for classes. Javassist
 * will not create a proxy for any final class or method - which is default in Kotlin. So note below that
 * the class AddressWithPeople is declared as open, and also the field people is declared open. Without these
 * declarations, lazy loading will fail.
 */

open class AddressWithPeople {
    var id: Int = 0
    lateinit var streetAddress: String
    lateinit var city: String
    lateinit var state: String
    open lateinit var people: List<Person>
}

class Person {
    var id: Int = 0
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var birthDate: LocalDate
    var employed: Boolean = false
    var occupation: String? = null
}

