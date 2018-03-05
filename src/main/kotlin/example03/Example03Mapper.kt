package example03

import org.apache.ibatis.annotations.*
import util.YesNoTypeHandler

interface Example03Mapper {

    @Select(
        "select p.id, p.first_name, p.last_name, p.birth_date, p.employed, p.occupation, a.id as address_id, a.street_address, a.city, a.state",
        "from Person p join Address a on p.address_id = a.id",
        "where p.id = #{value}"
    )
    @Results(
        Result(column = "id", property = "id"),
        Result(column = "first_name", property = "firstName"),
        Result(column = "last_name", property = "lastName"),
        Result(column = "birth_date", property="birthDate"),
        Result(column = "employed", property = "employed", typeHandler = YesNoTypeHandler::class),
        Result(column = "occupation", property="occupation"),
        Result(column = "address_id", property = "address.id"),
        Result(column = "street_address", property = "address.streetAddress"),
        Result(column = "city", property = "address.city"),
        Result(column = "state", property = "address.state")
    )
    fun selectPersonById(id: Int): PersonWithAddress
}
