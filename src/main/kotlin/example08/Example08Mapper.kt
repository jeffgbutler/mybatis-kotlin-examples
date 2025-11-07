package example08

interface Example08Mapper {
    fun selectAddressById(id: Int): AddressWithPeople

    fun selectPersonById(id: Int): PersonWithAddress
}
