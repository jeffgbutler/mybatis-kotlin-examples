package example02

import java.util.*

/*
 * In this version of the class, two things have changed:
 *
 * 1. The data type for birthDate has changed to java.util.Data
 * 2. The data type for employed has changed to Boolean
 *
 * Since these data types no longer match the query results, we need to alter the mapper to use @ConstructorArgs.
 *
 * For the birthDate field, a suitable type handler is built in to MyBatis, so simply specifying the proper data type
 * is all that's required.
 *
 * For the employed field, we will use a custom type handler.
 */
data class Person (val id: Int, val firstName: String, val lastName: String, val birthDate: Date, val employed: Boolean, val occupation: String?)
