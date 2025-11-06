package example07

import org.apache.ibatis.annotations.Arg
import org.apache.ibatis.annotations.Select
import util.YesNoTypeHandler
import java.time.LocalDate

interface Example07Mapper {

    @Select(
        "select id, first_name, last_name, birth_date, employed, occupation",
        "from Person where id = #{value}"
    )
    @Arg(column = "id", javaType = Int::class, id = true)
    @Arg(column = "first_name", javaType = String::class)
    @Arg(column = "last_name", javaType = String::class)
    @Arg(column = "birth_date", javaType = LocalDate::class)
    @Arg(column = "employed", javaType = Boolean::class, typeHandler = YesNoTypeHandler::class)
    @Arg(column = "occupation", javaType = String::class)
    fun selectPersonByIdWithAnnotations(id: Int): Person

    fun selectPersonByIdWithXMLMapping(id: Int): Person
}
