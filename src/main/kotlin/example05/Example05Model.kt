package example05

import java.util.*

data class PersonRecord(val id: Int? = null, val firstName: String? = null, val lastName: String? = null,
                        val birthDate: Date? = null, val employed: Boolean? = null,
                        val occupation: String? = null, val addressId: Int? = null)
