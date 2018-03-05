package example01

import java.sql.Date

/*
 * This shows that MyBatis automatic constructor mapping to Kotlin data classes is supported under certain limited
 * circumstances. What's important is that the order of fields in the query, and the data types, match exactly.
 *
 * Notes:
 * 1. occupation is nullable in the database, so should be String?
 * 2. birthDate here is java.sql.Date so the constructor can match via MyBatis' automatic constructor mapping.  This
 *    is not ideal - it can be resolved with @ConstructorArgs annotation as shown in example02
 */
data class Person (val id: Int, val firstName: String, val lastName: String, val birthDate: Date, val employed: String, val occupation: String?)
