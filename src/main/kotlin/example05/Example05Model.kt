package example05

import java.time.LocalDate

data class PersonRecord(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: LocalDate? = null,
    val employed: Boolean? = null,
    val occupation: String? = null,
    val addressId: Int? = null,
    val parentId: Int? = null
)
