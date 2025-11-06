package example07

import java.time.LocalDate

/*
 * In this example we will use explicit constructor mapping - both annotation and XML based.
 *
 * MyBatis has a built in type handler for java.sql.Date -> java.time.LocalDate so we don't need to write one.  But we do
 * need to write a type handler to convert the database string to a Boolean.  That type handler is
 * util.YesNoTypeHandler.
 */
data class Person(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val employed: Boolean,
    val occupation: String?
)
