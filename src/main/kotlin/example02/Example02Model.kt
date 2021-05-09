package example02

import java.time.LocalDate

/*
 * In this version of the class, one thing has changed:
 *
 * 1. The data type for employed has changed to Boolean
 *
 * MyBatis has a built in type handler for java.sql.Date -> java.time.LocalDate so we don't need to write one.  But we do
 * need to write a type handler to convert the database string to a Boolean.  That type handler is
 * util.YesNoTypeHandler.
 *
 * Note that MyBatis version prior to 3.5.0 will have difficulty finding the constructor for a class like this that
 * requires type handlers. For that case, see the example on example02.oldmybatis for a workaround.
 *
 */
data class Person(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val employed: Boolean,
    val occupation: String?
)