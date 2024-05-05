package util

import org.mybatis.dynamic.sql.AbstractSubselectCondition
import org.mybatis.dynamic.sql.BindableColumn
import org.mybatis.dynamic.sql.select.SelectModel
import org.mybatis.dynamic.sql.util.Buildable
import org.mybatis.dynamic.sql.util.kotlin.GroupingCriteriaCollector
import org.mybatis.dynamic.sql.util.kotlin.KotlinSubQueryBuilder

// The class and function in this file show how to extend the WHERE DSL with a Kotlin
// context receiver function. This is experimental in Kotlin 1.6.20

class MatchesAny<T>(selectModelBuilder: Buildable<SelectModel>) : AbstractSubselectCondition<T>(selectModelBuilder){
    override fun operator() = "= any"
}

context (GroupingCriteriaCollector)
infix fun <T> BindableColumn<T>.matchesAny(subQueryBuilder: KotlinSubQueryBuilder.() -> Unit) =
    invoke(MatchesAny(KotlinSubQueryBuilder().apply(subQueryBuilder)))
