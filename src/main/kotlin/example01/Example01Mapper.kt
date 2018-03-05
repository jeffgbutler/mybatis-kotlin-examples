package example01

import org.apache.ibatis.annotations.*

interface Example01Mapper {

    @Select(
        "select id, first_name, last_name, birth_date, employed, occupation",
        "from Person where id = #{value}"
    )
    fun selectPersonById(id: Int): Person
}
