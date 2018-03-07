package util

import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import org.apache.ibatis.type.TypeHandler

import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

@MappedTypes(Boolean::class)
@MappedJdbcTypes(JdbcType.VARCHAR)
class YesNoTypeHandler : TypeHandler<Boolean> {

    override fun setParameter(ps: PreparedStatement, i: Int, parameter: Boolean, jdbcType: JdbcType) {
        ps.setString(i, if (parameter) "Yes" else "No")
    }

    override fun getResult(rs: ResultSet, columnName: String): Boolean {
        return "Yes" == rs.getString(columnName)
    }

    override fun getResult(rs: ResultSet, columnIndex: Int): Boolean {
        return "Yes" == rs.getString(columnIndex)
    }

    override fun getResult(cs: CallableStatement, columnIndex: Int): Boolean {
        return "Yes" == cs.getString(columnIndex)
    }
}
