package example06

import example06.GeneratedAlwaysDynamicSqlSupport.firstName
import example06.GeneratedAlwaysDynamicSqlSupport.fullName
import example06.GeneratedAlwaysDynamicSqlSupport.generatedAlways
import example06.GeneratedAlwaysDynamicSqlSupport.id
import example06.GeneratedAlwaysDynamicSqlSupport.lastName
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
import org.mybatis.dynamic.sql.util.kotlin.CountCompleter
import org.mybatis.dynamic.sql.util.kotlin.DeleteCompleter
import org.mybatis.dynamic.sql.util.kotlin.KotlinUpdateBuilder
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.deleteFrom
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insert
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insertBatch
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insertMultipleWithGeneratedKeys
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectDistinct
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectOne
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.update
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

fun GeneratedAlwaysMapper.count(completer: CountCompleter) =
    countFrom(this::count, generatedAlways, completer)

fun GeneratedAlwaysMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, generatedAlways, completer)

fun GeneratedAlwaysMapper.deleteByPrimaryKey(id_: Int) =
    delete {
        where { id isEqualTo id_ }
    }

fun GeneratedAlwaysMapper.insert(row: GeneratedAlwaysRow) =
    insert(
        this::insert,
        row,
        generatedAlways
    ) {
        map(firstName) toProperty "firstName"
        map(lastName) toProperty "lastName"
    }

fun GeneratedAlwaysMapper.insertBatch(vararg records: GeneratedAlwaysRow) =
    insertBatch(records.toList())

fun GeneratedAlwaysMapper.insertBatch(records: List<GeneratedAlwaysRow>) =
    insertBatch(
        this::insert,
        records,
        generatedAlways
    ) {
        map(firstName) toProperty "firstName"
        map(lastName) toProperty "lastName"
    }

fun GeneratedAlwaysMapper.insertMultiple(vararg records: GeneratedAlwaysRow) =
    insertMultiple(records.toList())

fun GeneratedAlwaysMapper.insertMultiple(records: List<GeneratedAlwaysRow>) =
    insertMultipleWithGeneratedKeys(
        this::insertMultiple,
        records,
        generatedAlways
    ) {
        map(firstName) toProperty "firstName"
        map(lastName) toProperty "lastName"
    }

private val columnList = listOf(
    id,
    firstName,
    lastName,
    fullName
)

fun GeneratedAlwaysMapper.selectOne(completer: SelectCompleter) =
    selectOne(
        this::selectOne,
        columnList,
        generatedAlways,
        completer
    )

fun GeneratedAlwaysMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, generatedAlways, completer)

fun GeneratedAlwaysMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(
        this::selectMany,
        columnList,
        generatedAlways,
        completer
    )

fun GeneratedAlwaysMapper.selectByPrimaryKey(id_: Int) =
    selectOne {
        where { id isEqualTo id_ }
    }

fun GeneratedAlwaysMapper.update(completer: UpdateCompleter) =
    update(
        this::update,
        generatedAlways,
        completer
    )

fun GeneratedAlwaysMapper.updateByPrimaryKey(row: GeneratedAlwaysRow) =
    update {
        set(firstName) equalToOrNull row::firstName
        set(lastName) equalToOrNull row::lastName
        where { id isEqualTo row.id!! }
    }

fun GeneratedAlwaysMapper.updateByPrimaryKeySelective(row: GeneratedAlwaysRow) =
    update {
        set(firstName) equalToWhenPresent row::firstName
        set(lastName) equalToWhenPresent row::lastName
        where { id isEqualTo row.id!! }
    }

fun KotlinUpdateBuilder.updateAllColumns(row: GeneratedAlwaysRow) =
    apply {
        set(firstName) equalToOrNull row::firstName
        set(lastName) equalToOrNull row::lastName
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(row: GeneratedAlwaysRow) =
    apply {
        set(firstName) equalToWhenPresent row::firstName
        set(lastName) equalToWhenPresent row::lastName
    }
