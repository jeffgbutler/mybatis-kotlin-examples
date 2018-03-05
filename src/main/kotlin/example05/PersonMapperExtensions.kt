package example05

import org.apache.ibatis.session.RowBounds
import org.mybatis.dynamic.sql.SqlBuilder
import org.mybatis.dynamic.sql.SqlBuilder.*
import org.mybatis.dynamic.sql.delete.DeleteDSL
import org.mybatis.dynamic.sql.delete.MyBatis3DeleteModelAdapter
import org.mybatis.dynamic.sql.render.RenderingStrategy
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter
import org.mybatis.dynamic.sql.select.QueryExpressionDSL
import org.mybatis.dynamic.sql.select.SelectDSL
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.update.MyBatis3UpdateModelAdapter
import org.mybatis.dynamic.sql.update.UpdateDSL
import example05.PersonDynamicSqlSupport.Person
import example05.PersonDynamicSqlSupport.Person.addressId
import example05.PersonDynamicSqlSupport.Person.birthDate
import example05.PersonDynamicSqlSupport.Person.employed
import example05.PersonDynamicSqlSupport.Person.firstName
import example05.PersonDynamicSqlSupport.Person.id
import example05.PersonDynamicSqlSupport.Person.lastName
import example05.PersonDynamicSqlSupport.Person.occupation
import java.util.function.Function

private fun PersonMapper.selectManyWithRowbounds(rowBounds: RowBounds) = Function{ ss: SelectStatementProvider -> selectManyWithRowbounds(ss, rowBounds)}

fun PersonMapper.countByExample(): QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> {
    return SelectDSL.selectWithMapper(Function(this::count), SqlBuilder.count())
            .from(Person)
}

fun PersonMapper.deleteByExample(): DeleteDSL<MyBatis3DeleteModelAdapter<Int>> {
    return DeleteDSL.deleteFromWithMapper(this::delete, Person)
}

fun PersonMapper.deleteByPrimaryKey(id_: Int): Int {
    return DeleteDSL.deleteFromWithMapper(this::delete, Person)
            .where(id, SqlBuilder.isEqualTo(id_))
            .build()
            .execute()
}

fun PersonMapper.insert(record: PersonRecord): Int {
    return insert(SqlBuilder.insert(record)
            .into(Person)
            .map(id).toProperty("id")
            .map(firstName).toProperty("firstName")
            .map(lastName).toProperty("lastName")
            .map(birthDate).toProperty("birthDate")
            .map(employed).toProperty("employed")
            .map(occupation).toProperty("occupation")
            .map(addressId).toProperty("addressId")
            .build()
            .render(RenderingStrategy.MYBATIS3))
}

fun PersonMapper.selectByExample(): QueryExpressionDSL<MyBatis3SelectModelAdapter<List<PersonRecord>>> {
    return SelectDSL.selectWithMapper(Function(this::selectMany),id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)
            .from(Person)
}

fun PersonMapper.selectByExample(rowBounds: RowBounds): QueryExpressionDSL<MyBatis3SelectModelAdapter<List<PersonRecord>>> {
    return SelectDSL.selectWithMapper(selectManyWithRowbounds(rowBounds), id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)
            .from(Person)
}

fun PersonMapper.selectDistinctByExample(): QueryExpressionDSL<MyBatis3SelectModelAdapter<List<PersonRecord>>> {
    return SelectDSL.selectDistinctWithMapper(Function(this::selectMany), id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)
            .from(Person)
}

fun PersonMapper.selectDistinctByExample(rowBounds: RowBounds): QueryExpressionDSL<MyBatis3SelectModelAdapter<List<PersonRecord>>> {
    return SelectDSL.selectDistinctWithMapper(selectManyWithRowbounds(rowBounds), id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)
            .from(Person)
}

fun PersonMapper.selectByPrimaryKey(id_: Int): PersonRecord {
    return SelectDSL.selectWithMapper(Function(this::selectOne), id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)
            .from(Person)
            .where(id, isEqualTo(id_))
            .build()
            .execute()
}

fun PersonMapper.updateByExample(record: PersonRecord): UpdateDSL<MyBatis3UpdateModelAdapter<Int>> {
    return UpdateDSL.updateWithMapper(this::update, Person)
            .set(id).equalTo(record::id)
            .set(firstName).equalTo(record::firstName)
            .set(lastName).equalTo(record::lastName)
            .set(birthDate).equalTo(record::birthDate)
            .set(employed).equalTo(record::employed)
            .set(occupation).equalTo(record::occupation)
            .set(addressId).equalTo(record::addressId)
}

fun PersonMapper.updateByPrimaryKey(record: PersonRecord): Int {
    return UpdateDSL.updateWithMapper(this::update, Person)
            .set(firstName).equalTo(record::firstName)
            .set(lastName).equalTo(record::lastName)
            .set(birthDate).equalTo(record::birthDate)
            .set(employed).equalTo(record::employed)
            .set(occupation).equalTo(record::occupation)
            .set(addressId).equalTo(record::addressId)
            .where(id, isEqualTo(record::id))
            .build()
            .execute()
}

fun PersonMapper.updateByPrimaryKeySelective(record: PersonRecordForUpdate): Int {
    return UpdateDSL.updateWithMapper(this::update, Person)
            .set(firstName).equalToWhenPresent(record::firstName)
            .set(lastName).equalToWhenPresent(record::lastName)
            .set(birthDate).equalToWhenPresent(record::birthDate)
            .set(employed).equalToWhenPresent(record::employed)
            .set(occupation).equalToWhenPresent(record::occupation)
            .set(addressId).equalToWhenPresent(record::addressId)
            .where(id, isEqualTo(record::id))
            .build()
            .execute()
}
