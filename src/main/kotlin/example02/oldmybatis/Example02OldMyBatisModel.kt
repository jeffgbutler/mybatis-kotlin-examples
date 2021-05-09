package example02.oldmybatis

import org.apache.ibatis.annotations.AutomapConstructor
import java.util.*

/*
 * This version is the same as the version in example02 except for the following:
 *
 * 1. @AutomapConstructor annotation.
 * 2. The datatype of birthDate is java.util.Date - MyBatis versions prior to 3.4.5 did not include type
 *    handlers for the JSR310 types
 *
 * MyBatis versions prior to version 3.5.0 sometimes had difficulty finding constructors when there were
 * constructor parameters that required type handlers. In those cases, you can use the @AutomapConstructor annotation
 * to designate a constructor for MyBatis.
 *
 */
data class Person @AutomapConstructor constructor(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthDate: Date,
    val employed: Boolean,
    val occupation: String?
)