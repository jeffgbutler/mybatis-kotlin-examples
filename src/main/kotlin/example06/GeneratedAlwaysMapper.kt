package example06

import org.apache.ibatis.annotations.Flush
import org.apache.ibatis.annotations.InsertProvider
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.executor.BatchResult
import org.mybatis.dynamic.sql.insert.render.GeneralInsertStatementProvider
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper

// This mapper uses the common base mappers supplied by MyBatis Dynamic SQL.
// However, this mapper does NOT use the common insert mapper. When there are generated values
// in the table you must write your own insert methods and supply the @Options
// annotation if you with to retrieve generated values after the insert. MyBatis supports
// returning generated keys from any type of insert statement (single row, multi-row, and batch).
// The various insert statements below show how to code the @Options annotation properly
// for each case.

interface GeneratedAlwaysMapper: CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    @InsertProvider(type = SqlProviderAdapter::class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "row.id,row.fullName", keyColumn = "id,full_name")
    fun insert(insertStatement: InsertStatementProvider<GeneratedAlwaysRow>): Int

    @InsertProvider(type = SqlProviderAdapter::class, method = "generalInsert")
    @Options(useGeneratedKeys = true, keyProperty = "parameters.id,parameters.fullName", keyColumn = "id,full_name")
    fun generalInsert(insertStatement: GeneralInsertStatementProvider): Int

    @InsertProvider(type = SqlProviderAdapter::class, method = "insertMultipleWithGeneratedKeys")
    @Options(useGeneratedKeys = true, keyProperty = "records.id,records.fullName", keyColumn = "id,full_name")
    fun insertMultiple(insertStatement: String, @Param("records") records: List<GeneratedAlwaysRow>): Int

    @Flush
    fun flush(): List<BatchResult>

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "GeneratedAlwaysResult", value = [
            Result(column = "id", property = "id"),
            Result(column = "first_name", property = "firstName"),
            Result(column = "last_name", property = "lastName"),
            Result(column = "full_name", property = "fullName")
        ]
    )
    fun selectMany(selectStatement: SelectStatementProvider): List<GeneratedAlwaysRow>

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ResultMap("GeneratedAlwaysResult")
    fun selectOne(selectStatement: SelectStatementProvider): GeneratedAlwaysRow?
}
