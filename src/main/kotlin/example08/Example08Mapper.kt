package example08

import org.apache.ibatis.annotations.Arg
import org.apache.ibatis.annotations.ConstructorArgs
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.Select
import util.YesNoTypeHandler
import java.time.LocalDate

interface Example08Mapper {
    fun selectAddressById(id: Int): AddressWithPeople

    @Arg(column = "person_id", javaType = Int::class)
    @Arg(column = "first_name", javaType = String::class)
    @Arg(column = "last_name", javaType = String::class)
    @Arg(column = "birth_date", javaType = LocalDate::class)
    @Arg(column = "employed", javaType = Boolean::class, typeHandler = YesNoTypeHandler::class)
    @Arg(column = "occupation", javaType = String::class)
    @Arg(resultMap = "AddressResult")
    @Select(
        "select a.id, a.street_address, a.city, a.state, p.id as person_id, p.first_name, p.last_name, p.birth_date, p.employed, p.occupation",
        "from Address a join Person p on a.id = p.address_id",
        "where p.id = #{value}"
    )
    fun selectPersonById(id: Int): PersonWithAddress

    @Results(id = "AddressResult")
    @ConstructorArgs(value = [
        Arg(column = "id", javaType = Int::class, id = true),
        Arg(column = "street_address", javaType = String::class),
        Arg(column = "city", javaType = String::class),
        Arg(column = "state", javaType = String::class)
    ])
    @Select("dummy")
    fun dummyAddressResult() : Address
}
