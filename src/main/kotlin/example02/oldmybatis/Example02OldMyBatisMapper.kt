package example02.oldmybatis

import org.apache.ibatis.annotations.Select

interface Example02OldMyBatisMapper {

    @Select(
        "select id, first_name, last_name, birth_date, employed, occupation",
        "from Person where id = #{value}"
    )
    fun selectPersonById(id: Int): Person
}
