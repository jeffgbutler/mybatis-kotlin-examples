package example06

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType

object GeneratedAlwaysDynamicSqlSupport {
    val generatedAlways = GeneratedAlways()
    val id = generatedAlways.id
    val firstName = generatedAlways.firstName
    val lastName = generatedAlways.lastName
    val fullName = generatedAlways.fullName

    class GeneratedAlways : SqlTable("GeneratedAlways") {
        val id = column<Int>(name = "id", jdbcType = JDBCType.INTEGER)
        val firstName = column<String>(name = "first_name", jdbcType = JDBCType.VARCHAR)
        val lastName = column<String>(name = "last_name", jdbcType = JDBCType.VARCHAR)
        val fullName = column<String>(name = "full_name", jdbcType = JDBCType.VARCHAR)
    }
}
