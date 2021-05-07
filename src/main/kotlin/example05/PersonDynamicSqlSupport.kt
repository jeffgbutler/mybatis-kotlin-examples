package example05

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType
import java.util.*

object PersonDynamicSqlSupport {
    val person = Person()
    val id = person.id
    val firstName = person.firstName
    val lastName = person.lastName
    val birthDate = person.birthDate
    val employed = person.employed
    val occupation = person.occupation
    val addressId = person.addressId
    val parentId = person.parentId

    class Person : SqlTable("Person") {
        val id = column<Int>(name = "id", jdbcType = JDBCType.INTEGER)
        val firstName = column<String>(name = "first_name", jdbcType = JDBCType.VARCHAR)
        val lastName = column<String>(name = "last_name", jdbcType = JDBCType.VARCHAR)
        val birthDate = column<Date>(name = "birth_date", jdbcType = JDBCType.DATE)
        val employed = column<Boolean>(
            name = "employed",
            jdbcType = JDBCType.VARCHAR,
            typeHandler = "util.YesNoTypeHandler"
        )
        val occupation = column<String>(name = "occupation", jdbcType = JDBCType.VARCHAR)
        val addressId = column<Int>(name = "address_id", jdbcType = JDBCType.INTEGER)
        val parentId = column<Int>(name = "parent_id", jdbcType = JDBCType.INTEGER)
    }
}
