package example05

import org.mybatis.dynamic.sql.SqlColumn
import org.mybatis.dynamic.sql.SqlTable
import java.sql.JDBCType
import java.util.*

object PersonDynamicSqlSupport {
    object Person : SqlTable("Person") {
        // the 'as' casts are to avoid a warning related to platform types
        val id = column<Int>("id", JDBCType.INTEGER) as SqlColumn<Int>
        val firstName = column<String>("first_name", JDBCType.VARCHAR) as SqlColumn<String>
        val lastName = column<String>("last_name", JDBCType.VARCHAR) as SqlColumn<String>
        val birthDate = column<Date>("birth_date", JDBCType.DATE) as SqlColumn<Date>
        val employed = column<Boolean>("employed", JDBCType.VARCHAR, "util.YesNoTypeHandler") as SqlColumn<Boolean>
        val occupation = column<String>("occupation", JDBCType.VARCHAR) as SqlColumn<String>
        val addressId = column<Int>("address_id", JDBCType.INTEGER) as SqlColumn<Int>
    }
}
