package example03

import java.util.*

/*
 * In this version of the model, we have added an Address class and then made address on attribute of a person.
 * Kotlin's data classes require all attributes to be initialized on the constructor and this is incompatible
 * with how MyBatis builds result objects.  Therefore, the model is now plain Kotlin classes.
 *
 * Because we are no longer using constructor based classes, we have switched from @ConstructorArgs to @Results in
 * the mapper interface.
 *
 * Note the use of lateinit - this allows the class to be constructed, but the values of the properties can be set
 * after construction.  This works well, but has some limitations:
 *
 * 1. Primitive data types (Int and Boolean in this case) cannot have lateinit values - se we set a default
 * 2. Nullable fields (occupation in this case) cannot have lateinit - se we set them to null by default
 * 3. Nested classes (the address attribute of PersonWithAddress) should be initialized in their enclosing classes.
 *    Kotlin would allow lateinit for nested classes, but that won't work with MyBatis because MyBatis will do a "get"
 *    on the nested class, and "get" is not allowed until the class has been initialized
 */

class Address {
    var id: Int = 0
    lateinit var streetAddress: String
    lateinit var city: String
    lateinit var state: String
}

class PersonWithAddress {
    var id: Int = 0
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var birthDate: Date
    var employed: Boolean = false
    var occupation: String? = null
    var address = Address()
}

