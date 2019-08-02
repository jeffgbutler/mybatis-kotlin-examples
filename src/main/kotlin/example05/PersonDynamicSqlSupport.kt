package example05

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.kotlin.column
import java.sql.JDBCType
import java.util.*

object PersonDynamicSqlSupport {
    object Person : SqlTable("Person") {
        val id = column("id", JDBCType.INTEGER, Int::class)
        val firstName = column("first_name", JDBCType.VARCHAR, String::class)
        val lastName = column("last_name", JDBCType.VARCHAR, String::class)
        val birthDate = column("birth_date", JDBCType.DATE, Date::class)
        val employed = column("employed", JDBCType.VARCHAR, "util.YesNoTypeHandler", Boolean::class)
        val occupation = column("occupation", JDBCType.VARCHAR, String::class)
        val addressId = column("address_id", JDBCType.INTEGER, Int::class)
    }
}
