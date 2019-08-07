package org.mybatis.dynamic.sql.kotlin

import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.SortSpecification
import org.mybatis.dynamic.sql.SqlColumn
import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.delete.DeleteDSL
import org.mybatis.dynamic.sql.delete.DeleteModel
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider
import org.mybatis.dynamic.sql.render.RenderingStrategy
import org.mybatis.dynamic.sql.select.QueryExpressionDSL
import org.mybatis.dynamic.sql.select.SelectDSL
import org.mybatis.dynamic.sql.select.SelectModel
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.update.UpdateDSL
import org.mybatis.dynamic.sql.update.UpdateModel
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider
import org.mybatis.dynamic.sql.util.Buildable
import java.sql.JDBCType
import kotlin.reflect.KClass

typealias CountHelper = QueryExpressionDSL<SelectModelAdapter<Long>>.() -> Buildable<SelectModelAdapter<Long>>
typealias DeleteHelper = DeleteDSL<DeleteModelAdapter>.() -> Buildable<DeleteModelAdapter>
typealias SelectListHelper<T> = QueryExpressionDSL<SelectModelAdapter<List<T>>>.() -> Buildable<SelectModelAdapter<List<T>>>
typealias SelectOneHelper<T> = QueryExpressionDSL<SelectModelAdapter<T?>>.() -> Buildable<SelectModelAdapter<T?>>
typealias UpdateHelper = UpdateDSL<UpdateModelAdapter>.() -> Buildable<UpdateModelAdapter>

class DeleteModelAdapter(private val deleteModel: DeleteModel, private val mapperMethod: (DeleteStatementProvider) -> Int) {
    fun execute() = mapperMethod(deleteModel.render(RenderingStrategy.MYBATIS3))
}

class SelectModelAdapter<R>(private val selectModel: SelectModel, private val mapperMethod: (SelectStatementProvider) -> R) {
    fun execute() = mapperMethod(selectModel.render(RenderingStrategy.MYBATIS3))
}

class UpdateModelAdapter(private val updateModel: UpdateModel, private val mapperMethod: (UpdateStatementProvider) -> Int) {
    fun execute() = mapperMethod(updateModel.render(RenderingStrategy.MYBATIS3))
}

fun <R> selectWithKotlinMapper(mapperMethod: (SelectStatementProvider) -> R, vararg selectList: BasicColumn): QueryExpressionDSL.FromGatherer<SelectModelAdapter<R>> {
    return SelectDSL.select({ SelectModelAdapter(it, mapperMethod) }, selectList)
}

fun <R> selectDistinctWithKotlinMapper(mapperMethod: (SelectStatementProvider) -> R, vararg selectList: BasicColumn): QueryExpressionDSL.FromGatherer<SelectModelAdapter<R>> {
    return SelectDSL.selectDistinct({ SelectModelAdapter(it, mapperMethod) }, selectList)
}

fun deleteWithKotlinMapper(mapperMethod: (DeleteStatementProvider) -> Int, table: SqlTable): DeleteDSL<DeleteModelAdapter> {
    return DeleteDSL.deleteFrom({ DeleteModelAdapter(it, mapperMethod) }, table)
}

fun updateWithKotlinMapper(mapperMethod: (UpdateStatementProvider) -> Int, table: SqlTable): UpdateDSL<UpdateModelAdapter> {
    return UpdateDSL.update({ UpdateModelAdapter(it, mapperMethod) }, table)
}

fun countAllRows(): CountHelper = { this }
fun deleteAllRows(): DeleteHelper = { this }
fun <T> selectAllRows(): SelectListHelper<T> = { this }
fun <T> selectAllRowsOrderedBy(vararg columns: SortSpecification): SelectListHelper<T> = { orderBy(*columns) }

fun <T : Any> SqlTable.column(name: String, type: KClass<T>): SqlColumn<T> =
        SqlColumn.of(name, this)

fun <T : Any> SqlTable.column(name: String, jdbcType: JDBCType, type: KClass<T>): SqlColumn<T> =
        SqlColumn.of(name, this, jdbcType)

fun <T : Any> SqlTable.column(name: String, jdbcType: JDBCType, typeHandler: String, type: KClass<T>): SqlColumn<T> =
        SqlColumn.of<T>(name, this, jdbcType).withTypeHandler(typeHandler)
