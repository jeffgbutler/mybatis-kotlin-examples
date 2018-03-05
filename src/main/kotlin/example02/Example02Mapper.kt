package example02

import org.apache.ibatis.annotations.*
import util.YesNoTypeHandler
import java.util.*

interface Example02Mapper {

    @Select(
        "select id, first_name, last_name, birth_date, employed, occupation",
        "from Person where id = #{value}"
    )
    @ConstructorArgs(
        Arg(column = "id", javaType = Int::class),
        Arg(column = "first_name", javaType = String::class),
        Arg(column = "last_name", javaType = String::class),
        Arg(column = "birth_date", javaType = Date::class),
        Arg(column = "employed", javaType = Boolean::class, typeHandler = YesNoTypeHandler::class),
        Arg(column = "occupation", javaType = String::class)
    )
    fun selectPersonById(id: Int): Person
}
