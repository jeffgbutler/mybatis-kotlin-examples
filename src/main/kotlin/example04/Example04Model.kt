package example04

import java.util.*

/*
 * In this version of the model, we have an AddressWithPeople class that includes a nested list of the Persons with the
 * address. This shows how to code a join.  You will note that the mapper makes reference to an XML file for the result
 * mapping - this is because the MyBatis annotations cannot be used to describe nested collections.
 */

class AddressWithPeople {
    var id: Int = 0
    lateinit var streetAddress: String
    lateinit var city: String
    lateinit var state: String
    val people = mutableListOf<Person>()
}

class Person {
    var id: Int = 0
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var birthDate: Date
    var employed: Boolean = false
    var occupation: String? = null
}

