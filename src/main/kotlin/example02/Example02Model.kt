package example02

import org.apache.ibatis.annotations.AutomapConstructor
import java.util.*

/*
 * In this version of the class, two things have changed:
 *
 * 1. The data type for birthDate has changed to java.util.Date
 * 2. The data type for employed has changed to Boolean
 *
 * MyBatis has a built in type handler for java.sql.Date -> java.util.Date so we don't need to write one.  But we do need to write
 * a type handler to convert the database string to a Boolean.  That type handler is util.YesNoTypeHandler.
 *
 * We also need to use the @AutomapConstructor annotation to enable advanced auto mapping in MyBatis
 *
 */
data class Person @AutomapConstructor constructor(val id: Int, val firstName: String, val lastName: String, val birthDate: Date, val employed: Boolean, val occupation: String?)