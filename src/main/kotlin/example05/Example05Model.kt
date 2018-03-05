package example05

import java.util.*

data class PersonRecord (val id: Int, val firstName: String, val lastName: String, val birthDate: Date, val employed: Boolean, val occupation: String?, val addressId: Int)

data class PersonRecordForUpdate (val id: Int, var firstName: String? = null, var lastName: String? = null, var birthDate: Date? = null,
                                  var employed: Boolean? = null, var occupation: String? = null, var addressId: Int? = null) {
    constructor(personRecord: PersonRecord) : this(personRecord.id, personRecord.firstName, personRecord.lastName, personRecord.birthDate, personRecord.employed, personRecord.occupation, personRecord.addressId)
}