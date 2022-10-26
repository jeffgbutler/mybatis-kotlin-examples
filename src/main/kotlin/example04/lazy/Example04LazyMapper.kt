package example04.lazy

import org.apache.ibatis.annotations.*
import org.apache.ibatis.mapping.FetchType
import util.YesNoTypeHandler

// In this mapper we code two queries because we want to lazy load the nested list. This forces an
// N + 1 query, so use with caution

interface Example04LazyMapper {

    @Select(
        "select id, street_address, city, state",
        "from Address",
        "where id = #{value}"
    )
    @Results(
        Result(column = "id", property = "id", id = true),
        Result(column = "street_address", property = "streetAddress"),
        Result(column = "city", property = "city"),
        Result(column = "state", property = "state"),
        Result(column = "id", property = "people", many = Many(fetchType = FetchType.LAZY, select = "selectPeopleByAddressId" ))
    )
    fun selectAddressById(id: Int): AddressWithPeople

    @Select(
        "select id, first_name, last_name, birth_date, employed, occupation",
        "from Person",
        "where address_id = #{value}"
    )
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "first_name", property = "firstName"),
        Result(column = "last_name", property = "lastName"),
        Result(column = "birth_date", property = "birthDate"),
        Result(column = "employed", property = "employed", typeHandler = YesNoTypeHandler::class),
        Result(column = "occupation", property = "occupation")
    )
    fun selectPeopleByAddressId(addressId: Int): List<Person>
}
