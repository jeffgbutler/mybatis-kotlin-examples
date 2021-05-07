package example06

import example06.GeneratedAlwaysDynamicSqlSupport.generatedAlways
import example06.GeneratedAlwaysDynamicSqlSupport.id
import example06.GeneratedAlwaysDynamicSqlSupport.firstName
import example06.GeneratedAlwaysDynamicSqlSupport.fullName
import example06.GeneratedAlwaysDynamicSqlSupport.lastName
import org.mybatis.dynamic.sql.util.kotlin.CountCompleter
import org.mybatis.dynamic.sql.util.kotlin.DeleteCompleter
import org.mybatis.dynamic.sql.util.kotlin.KotlinUpdateBuilder
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter
import org.mybatis.dynamic.sql.util.kotlin.elements.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.countFrom
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.deleteFrom
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insert
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insertBatch
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insertMultipleWithGeneratedKeys
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectDistinct
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectOne
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.update

fun GeneratedAlwaysMapper.count(completer: CountCompleter) =
    countFrom(this::count, generatedAlways, completer)

fun GeneratedAlwaysMapper.delete(completer: DeleteCompleter) =
    deleteFrom(this::delete, generatedAlways, completer)

fun GeneratedAlwaysMapper.deleteByPrimaryKey(id_: Int) =
    delete {
        where(id, isEqualTo(id_))
    }

fun GeneratedAlwaysMapper.insert(row: GeneratedAlwaysRow) =
    insert(this::insert, row, generatedAlways) {
        map(firstName).toProperty("firstName")
        map(lastName).toProperty("lastName")
    }

fun GeneratedAlwaysMapper.insertBatch(vararg records: GeneratedAlwaysRow) =
    insertBatch(records.toList())

fun GeneratedAlwaysMapper.insertBatch(records: List<GeneratedAlwaysRow>) =
    insertBatch(this::insert, records, generatedAlways) {
        map(firstName).toProperty("firstName")
        map(lastName).toProperty("lastName")
    }

fun GeneratedAlwaysMapper.insertMultiple(vararg records: GeneratedAlwaysRow) =
    insertMultiple(records.toList())

fun GeneratedAlwaysMapper.insertMultiple(records: List<GeneratedAlwaysRow>) =
    insertMultipleWithGeneratedKeys(
        this::insertMultiple,
        records,
        generatedAlways
    ) {
        map(firstName).toProperty("firstName")
        map(lastName).toProperty("lastName")
    }

private val columnList = listOf(id, firstName, lastName, fullName)

fun GeneratedAlwaysMapper.selectOne(completer: SelectCompleter) =
    selectOne(this::selectOne, columnList, generatedAlways, completer)

fun GeneratedAlwaysMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, generatedAlways, completer)

fun GeneratedAlwaysMapper.selectDistinct(completer: SelectCompleter) =
    selectDistinct(this::selectMany, columnList, generatedAlways, completer)

fun GeneratedAlwaysMapper.selectByPrimaryKey(id_: Int) =
    selectOne {
        where(id, isEqualTo(id_))
    }

fun GeneratedAlwaysMapper.update(completer: UpdateCompleter) =
    update(this::update, generatedAlways, completer)

fun GeneratedAlwaysMapper.updateByPrimaryKey(row: GeneratedAlwaysRow) =
    update {
        set(firstName).equalToOrNull(row::firstName)
        set(lastName).equalToOrNull(row::lastName)
        where(id, isEqualTo(row.id!!))
    }

fun GeneratedAlwaysMapper.updateByPrimaryKeySelective(row: GeneratedAlwaysRow) =
    update {
        set(firstName).equalToWhenPresent(row::firstName)
        set(lastName).equalToWhenPresent(row::lastName)
        where(id, isEqualTo(row.id!!))
    }

fun KotlinUpdateBuilder.updateAllColumns(row: GeneratedAlwaysRow) =
    apply {
        set(firstName).equalToOrNull(row::firstName)
        set(lastName).equalToOrNull(row::lastName)
    }

fun KotlinUpdateBuilder.updateSelectiveColumns(row: GeneratedAlwaysRow) =
    apply {
        set(firstName).equalToWhenPresent(row::firstName)
        set(lastName).equalToWhenPresent(row::lastName)
    }
