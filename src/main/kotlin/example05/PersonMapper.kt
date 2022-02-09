package example05

import example05.PersonDynamicSqlSupport.addressId
import example05.PersonDynamicSqlSupport.birthDate
import example05.PersonDynamicSqlSupport.employed
import example05.PersonDynamicSqlSupport.firstName
import example05.PersonDynamicSqlSupport.id
import example05.PersonDynamicSqlSupport.lastName
import example05.PersonDynamicSqlSupport.occupation
import example05.PersonDynamicSqlSupport.parentId
import example05.PersonDynamicSqlSupport.person
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
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
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insertMultiple
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectDistinct
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectOne
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.update
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

fun PersonMapper.count(completer: CountCompleter) =
    countFrom(this::count, person, completer)

fun PersonMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, person, completer)

fun PersonMapper.deleteByPrimaryKey(id_: Int) =
    delete {
        where { id isEqualTo id_ }
    }

fun PersonMapper.insert(row: PersonRecord) =
    insert(this::insert, row, person) {
        map(id).toProperty("id")
        map(firstName).toProperty("firstName")
        map(lastName).toProperty("lastName")
        map(birthDate).toProperty("birthDate")
        map(employed).toProperty("employed")
        map(occupation).toProperty("occupation")
        map(addressId).toProperty("addressId")
        map(parentId).toProperty("parentId")
    }

fun PersonMapper.insertBatch(vararg records: PersonRecord) =
    insertBatch(records.toList())

fun PersonMapper.insertBatch(records: List<PersonRecord>) =
    insertBatch(this::insert, records, person) {
        map(id).toProperty("id")
        map(firstName).toProperty("firstName")
        map(lastName).toProperty("lastName")
        map(birthDate).toProperty("birthDate")
        map(employed).toProperty("employed")
        map(occupation).toProperty("occupation")
        map(addressId).toProperty("addressId")
        map(parentId).toProperty("parentId")
    }

fun PersonMapper.insertMultiple(vararg records: PersonRecord) =
    insertMultiple(records.toList())

fun PersonMapper.insertMultiple(records: List<PersonRecord>) =
    insertMultiple(
        this::insertMultiple,
        records,
        person
    ) {
        map(id).toProperty("id")
        map(firstName).toProperty("firstName")
        map(lastName).toProperty("lastName")
        map(birthDate).toProperty("birthDate")
        map(employed).toProperty("employed")
        map(occupation).toProperty("occupation")
        map(addressId).toProperty("addressId")
        map(parentId).toProperty("parentId")
    }

private val columnList = listOf(
    id.`as`("A_ID"),
    firstName,
    lastName,
    birthDate,
    employed,
    occupation,
    addressId
)

fun PersonMapper.selectOne(completer: SelectCompleter) =
    selectOne(
        this::selectOne,
        columnList,
        person,
        completer
    )

fun PersonMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, person, completer)

fun PersonMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(
        this::selectMany,
        columnList,
        person,
        completer
    )

fun PersonMapper.selectByPrimaryKey(id_: Int) =
    selectOne {
        where { id isEqualTo id_ }
    }

fun PersonMapper.update(completer: UpdateCompleter) =
    update(this::update, person, completer)

fun PersonMapper.updateByPrimaryKey(row: PersonRecord) =
    update {
        set(firstName) equalToOrNull row::firstName
        set(lastName) equalToOrNull row::lastName
        set(birthDate) equalToOrNull row::birthDate
        set(employed) equalToOrNull row::employed
        set(occupation) equalToOrNull row::occupation
        set(addressId) equalToOrNull row::addressId
        set(parentId) equalToOrNull row::parentId
        where { id isEqualTo row.id!! }
    }

fun PersonMapper.updateByPrimaryKeySelective(row: PersonRecord) =
    update {
        set(firstName) equalToWhenPresent row::firstName
        set(lastName) equalToWhenPresent row::lastName
        set(birthDate) equalToWhenPresent row::birthDate
        set(employed) equalToWhenPresent row::employed
        set(occupation) equalToWhenPresent row::occupation
        set(addressId) equalToWhenPresent row::addressId
        set(parentId) equalToWhenPresent row::parentId
        where { id isEqualTo row.id!! }
    }

fun KotlinUpdateBuilder.updateAllColumns(row: PersonRecord) =
    apply {
        set(id) equalToOrNull row::id
        set(firstName) equalToOrNull row::firstName
        set(lastName) equalToOrNull row::lastName
        set(birthDate) equalToOrNull row::birthDate
        set(employed) equalToOrNull row::employed
        set(occupation) equalToOrNull row::occupation
        set(addressId) equalToOrNull row::addressId
        set(parentId) equalToOrNull row::parentId
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(row: PersonRecord) =
    apply {
        set(id) equalToWhenPresent row::id
        set(firstName) equalToWhenPresent row::firstName
        set(lastName) equalToWhenPresent row::lastName
        set(birthDate) equalToWhenPresent row::birthDate
        set(employed) equalToWhenPresent row::employed
        set(occupation) equalToWhenPresent row::occupation
        set(addressId) equalToWhenPresent row::addressId
        set(parentId) equalToWhenPresent row::parentId
    }
