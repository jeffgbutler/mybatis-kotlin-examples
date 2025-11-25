package util

import org.mybatis.dynamic.sql.AbstractSubselectCondition
import org.mybatis.dynamic.sql.BindableColumn
import org.mybatis.dynamic.sql.select.SelectModel
import org.mybatis.dynamic.sql.util.Buildable
import org.mybatis.dynamic.sql.util.kotlin.GroupingCriteriaCollector
import org.mybatis.dynamic.sql.util.kotlin.KotlinSubQueryBuilder

class MatchesAny<T : Any>(selectModelBuilder: Buildable<SelectModel>) : AbstractSubselectCondition<T>(selectModelBuilder){
    override fun operator() = "= any"
}

// The class and function in this file show how to extend the WHERE DSL in various ways

// The following shows how to extend the DSL without using any experimental features.
// It is somewhat less elegant than the context parameters method shown below, but it doesn't require the use of an
// experimental compiler feature
fun <T: Any> BindableColumn<T>.matchesAny(
    collector: GroupingCriteriaCollector,
    subQueryBuilder: KotlinSubQueryBuilder.() -> Unit
) =
    with(collector) {
        invoke(MatchesAny(KotlinSubQueryBuilder().apply(subQueryBuilder)))
    }


// The following shows how to extend the DSL with the experimental Context Parameters method
// https://github.com/Kotlin/KEEP/blob/context-parameters/proposals/context-parameters.md
// This requires use of the compiler flag -Xcontext-parameters
context (collector: GroupingCriteriaCollector)
infix fun <T : Any> BindableColumn<T>.matchesAny(subQueryBuilder: KotlinSubQueryBuilder.() -> Unit) =
    with(collector) {
        invoke(MatchesAny(KotlinSubQueryBuilder().apply(subQueryBuilder)))
    }
