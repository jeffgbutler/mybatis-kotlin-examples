package org.mybatis.dynamic.sql.kotlin

import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.SortSpecification
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

fun <R> selectWithKotlinMapper(mapperMethod: (SelectStatementProvider) -> R, vararg selectList: BasicColumn) =
    SelectDSL.select({ SelectModelAdapter(it, mapperMethod) }, selectList) as QueryExpressionDSL.FromGatherer<SelectModelAdapter<R>>

fun <R> selectDistinctWithKotlinMapper(mapperMethod: (SelectStatementProvider) -> R, vararg selectList: BasicColumn) =
    SelectDSL.selectDistinct({ SelectModelAdapter(it, mapperMethod) }, selectList) as QueryExpressionDSL.FromGatherer<SelectModelAdapter<R>>

fun deleteWithKotlinMapper(mapperMethod: (DeleteStatementProvider) -> Int, table: SqlTable) =
    DeleteDSL.deleteFrom({ DeleteModelAdapter(it, mapperMethod) }, table) as DeleteDSL<DeleteModelAdapter>

fun updateWithKotlinMapper(mapperMethod: (UpdateStatementProvider) -> Int, table: SqlTable) =
    UpdateDSL.update({ UpdateModelAdapter(it, mapperMethod) }, table) as UpdateDSL<UpdateModelAdapter>

fun DeleteDSL<DeleteModelAdapter>.allRows() = this as Buildable<DeleteModelAdapter>
fun <T> QueryExpressionDSL<SelectModelAdapter<T>>.allRows() = this as Buildable<SelectModelAdapter<T>>
fun <T> QueryExpressionDSL<SelectModelAdapter<T>>.allRowsOrderedBy(vararg columns: SortSpecification) =
        orderBy(*columns) as Buildable<SelectModelAdapter<T>>
