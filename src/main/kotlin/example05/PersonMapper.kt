package example05

import org.apache.ibatis.annotations.*
import org.apache.ibatis.session.RowBounds
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import util.YesNoTypeHandler
import java.util.*

interface PersonMapper {
    @InsertProvider(type = SqlProviderAdapter::class, method = "insert")
    fun insert(insertStatement: InsertStatementProvider<PersonRecord>): Int

    @UpdateProvider(type = SqlProviderAdapter::class, method = "update")
    fun update(updateStatement: UpdateStatementProvider): Int

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ConstructorArgs(
            Arg(column = "A_ID", javaType = Int::class, id = true),
            Arg(column = "first_name", javaType = String::class),
            Arg(column = "last_name", javaType = String::class),
            Arg(column = "birth_date", javaType = Date::class),
            Arg(column = "employed", javaType = Boolean::class, typeHandler = YesNoTypeHandler::class),
            Arg(column = "occupation", javaType = String::class),
            Arg(column = "address_id", javaType = Int::class)
    )
    fun selectMany(selectStatement: SelectStatementProvider): List<PersonRecord>

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ConstructorArgs(
            Arg(column = "A_ID", javaType = Int::class, id = true),
            Arg(column = "first_name", javaType = String::class),
            Arg(column = "last_name", javaType = String::class),
            Arg(column = "birth_date", javaType = Date::class),
            Arg(column = "employed", javaType = Boolean::class, typeHandler = YesNoTypeHandler::class),
            Arg(column = "occupation", javaType = String::class),
            Arg(column = "address_id", javaType = Int::class)
    )
    fun selectManyWithRowbounds(selectStatement: SelectStatementProvider, rowBounds: RowBounds): List<PersonRecord>

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ConstructorArgs(
            Arg(column = "A_ID", javaType = Int::class, id = true),
            Arg(column = "first_name", javaType = String::class),
            Arg(column = "last_name", javaType = String::class),
            Arg(column = "birth_date", javaType = Date::class),
            Arg(column = "employed", javaType = Boolean::class, typeHandler = YesNoTypeHandler::class),
            Arg(column = "occupation", javaType = String::class),
            Arg(column = "address_id", javaType = Int::class)
    )
    fun selectOne(selectStatement: SelectStatementProvider): PersonRecord

    @DeleteProvider(type = SqlProviderAdapter::class, method = "delete")
    fun delete(deleteStatement: DeleteStatementProvider): Int

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    fun count(selectStatement: SelectStatementProvider): Long
}
