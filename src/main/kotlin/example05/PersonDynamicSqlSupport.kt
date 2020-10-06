package example05

import org.mybatis.dynamic.sql.SqlTable
import java.sql.JDBCType
import java.util.*

object PersonDynamicSqlSupport {
    object Person : SqlTable("Person") {
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
