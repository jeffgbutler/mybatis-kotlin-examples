package example05

import org.mybatis.dynamic.sql.SqlTable
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
        val id = column<Int>("id", JDBCType.INTEGER)
        val firstName = column<String>("first_name", JDBCType.VARCHAR)
        val lastName = column<String>("last_name", JDBCType.VARCHAR)
        val birthDate = column<Date>("birth_date", JDBCType.DATE)
        val employed = column<Boolean>("employed", JDBCType.VARCHAR, "util.YesNoTypeHandler")
        val occupation = column<String>("occupation", JDBCType.VARCHAR)
        val addressId = column<Int>("address_id", JDBCType.INTEGER)
        val parentId = column<Int>("parent_id", JDBCType.INTEGER)
    }
}
