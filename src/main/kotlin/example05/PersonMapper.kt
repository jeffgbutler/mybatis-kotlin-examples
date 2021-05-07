package example05

import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper
import util.YesNoTypeHandler

// This mapper uses the common base mappers supplied by MyBatis Dynamic SQL.
// Use of the common insert mapper is appropriate when there are NOT generated values in the table.
// In this case, only the select methods need to be written because we need to supply a
// result map.

interface PersonMapper : CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<PersonRecord>, CommonUpdateMapper {
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "PersonRecordResult", value = [
            Result(column = "a_id", property = "id"),
            Result(column = "first_name", property = "firstName"),
            Result(column = "last_name", property = "lastName"),
            Result(column = "birth_date", property = "birthDate"),
            Result(column = "employed", property = "employed", typeHandler = YesNoTypeHandler::class),
            Result(column = "occupation", property = "occupation"),
            Result(column = "address_id", property = "addressId"),
            Result(column = "parent_id", property = "parentId")
        ]
    )
    fun selectMany(selectStatement: SelectStatementProvider): List<PersonRecord>

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ResultMap("PersonRecordResult")
    fun selectOne(selectStatement: SelectStatementProvider): PersonRecord?
}
