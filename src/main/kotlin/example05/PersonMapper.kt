package example05

import org.apache.ibatis.annotations.*
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper
import util.YesNoTypeHandler

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
