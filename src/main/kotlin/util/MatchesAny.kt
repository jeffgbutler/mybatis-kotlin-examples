package util

import org.mybatis.dynamic.sql.AbstractSubselectCondition
import org.mybatis.dynamic.sql.BindableColumn
import org.mybatis.dynamic.sql.select.SelectModel
import org.mybatis.dynamic.sql.util.Buildable
import org.mybatis.dynamic.sql.util.kotlin.GroupingCriteriaCollector
import org.mybatis.dynamic.sql.util.kotlin.KotlinSubQueryBuilder

class MatchesAny<T>(selectModelBuilder: Buildable<SelectModel>) : AbstractSubselectCondition<T>(selectModelBuilder){
    override fun operator() = "= any"
}

// The class and function in this file show how to extend the WHERE DSL

// This should eventually be easier when Kotlin introduces Context Parameters
// https://github.com/Kotlin/KEEP/blob/context-parameters/proposals/context-parameters.md

fun <T> BindableColumn<T>.matchesAny(
    collector: GroupingCriteriaCollector,
    subQueryBuilder: KotlinSubQueryBuilder.() -> Unit
) =
    with(collector) {
        invoke(MatchesAny(KotlinSubQueryBuilder().apply(subQueryBuilder)))
    }


// The following shows how to do it with the deprecated Context Receivers method
// We keep this as reference to show a desired future state once Kotlin has a good solution to the
// problem of multiple contexts. This required use of the compiler flag -Xcontext-receivers

//context (GroupingCriteriaCollector)
//infix fun <T> BindableColumn<T>.matchesAny(subQueryBuilder: KotlinSubQueryBuilder.() -> Unit) =
//    invoke(MatchesAny(KotlinSubQueryBuilder().apply(subQueryBuilder)))
