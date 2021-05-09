package example01

import java.time.LocalDate

/*
 * This shows that MyBatis automatic constructor mapping to Kotlin data classes is supported under many
 * circumstances. What's important is that the order of fields in the query, and the data types, match exactly,
 * or match types with registered type handlers.
 *
 * Notes:
 * 1. occupation is nullable in the database, so the Kotlin type should be String?
 * 2. birthDate here is java.time.LocalDate. MyBatis has built-in type handlers for many data types so no special
 *    configuration is needed here.
 * 3. MyBatis versions prior to 3.5.0 will have difficulty finding the constructor in this class.
 *    For that case, see the example in example02.oldmybatis f0r details on how to resolve that
 *    issue.
 */
data class Person(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val employed: String,
    val occupation: String?
)
