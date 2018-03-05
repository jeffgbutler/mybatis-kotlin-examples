package example04

import org.apache.ibatis.annotations.*
import util.YesNoTypeHandler

interface Example04Mapper {

    @Select(
        "select a.id, a.street_address, a.city, a.state, p.id as person_id, p.first_name, p.last_name, p.birth_date, p.employed, p.occupation",
        "from Address a join Person p on a.id = p.address_id",
        "where a.id = #{value}"
    )
    @ResultMap("AddressWithPeopleResult")
    fun selectAddressById(id: Int): AddressWithPeople
}
