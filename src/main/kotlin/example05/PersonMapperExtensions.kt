package example05

import example05.PersonDynamicSqlSupport.Person
import example05.PersonDynamicSqlSupport.Person.addressId
import example05.PersonDynamicSqlSupport.Person.birthDate
import example05.PersonDynamicSqlSupport.Person.employed
import example05.PersonDynamicSqlSupport.Person.firstName
import example05.PersonDynamicSqlSupport.Person.id
import example05.PersonDynamicSqlSupport.Person.lastName
import example05.PersonDynamicSqlSupport.Person.occupation
import org.mybatis.dynamic.sql.SqlBuilder
import org.mybatis.dynamic.sql.SqlBuilder.count
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.kotlin.*
import org.mybatis.dynamic.sql.render.RenderingStrategy

fun PersonMapper.countByExample(helper: CountByExampleHelper) =
        helper(selectWithKotlinMapper(this::count, count())
                .from(Person))
                .build()
                .execute()

fun PersonMapper.deleteByExample(helper: DeleteByExampleHelper) =
        helper(deleteWithKotlinMapper(this::delete, Person))
                .build()
                .execute()

fun PersonMapper.deleteByPrimaryKey(id_: Int) =
        deleteWithKotlinMapper(this::delete, Person)
                .where(id, isEqualTo(id_))
                .build()
                .execute()

fun PersonMapper.insert(record: PersonRecord) =
        insert(SqlBuilder.insert(record)
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

fun PersonMapper.insertMultiple(vararg records: PersonRecord) =
        insertMultiple(SqlBuilder.insertMultiple(*records)
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

fun PersonMapper.selectByExample(helper: SelectByExampleHelper<PersonRecord>) =
        helper(selectWithKotlinMapper(this::selectMany, id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)
                .from(Person))
                .build()
                .execute()

fun PersonMapper.selectDistinctByExample(helper: SelectByExampleHelper<PersonRecord>) =
        helper(selectDistinctWithKotlinMapper(this::selectMany, id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)
                .from(Person))
                .build()
                .execute()

fun PersonMapper.selectByPrimaryKey(id_: Int) =
        selectWithKotlinMapper(this::selectOne, id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)
                .from(Person)
                .where(id, isEqualTo(id_))
                .build()
                .execute()

fun PersonMapper.updateByExample(helper: UpdateByExampleHelper): UpdateByExampleCompleter<PersonRecord> =
        UpdateByExampleCompleter(Person, helper, this::update, { record, dsl ->
            dsl.set(id).equalTo(record::id)
                    .set(firstName).equalTo(record::firstName)
                    .set(lastName).equalTo(record::lastName)
                    .set(birthDate).equalTo(record::birthDate)
                    .set(employed).equalTo(record::employed)
                    .set(occupation).equalTo(record::occupation)
                    .set(addressId).equalTo(record::addressId)
        })

fun PersonMapper.updateByExampleSelective(helper: UpdateByExampleHelper): UpdateByExampleCompleter<PersonRecord> =
        UpdateByExampleCompleter(Person, helper, this::update, { record, dsl ->
            dsl.set(id).equalToWhenPresent(record::id)
                    .set(firstName).equalToWhenPresent(record::firstName)
                    .set(lastName).equalToWhenPresent(record::lastName)
                    .set(birthDate).equalToWhenPresent(record::birthDate)
                    .set(employed).equalToWhenPresent(record::employed)
                    .set(occupation).equalToWhenPresent(record::occupation)
                    .set(addressId).equalToWhenPresent(record::addressId)
        })

fun PersonMapper.updateByPrimaryKey(record: PersonRecord) =
        updateWithKotlinMapper(this::update, Person)
                .set(firstName).equalTo(record::firstName)
                .set(lastName).equalTo(record::lastName)
                .set(birthDate).equalTo(record::birthDate)
                .set(employed).equalTo(record::employed)
                .set(occupation).equalTo(record::occupation)
                .set(addressId).equalTo(record::addressId)
                .where(id, isEqualTo(record::id))
                .build()
                .execute()

fun PersonMapper.updateByPrimaryKeySelective(record: PersonRecord) =
        updateWithKotlinMapper(this::update, Person)
                .set(firstName).equalToWhenPresent(record::firstName)
                .set(lastName).equalToWhenPresent(record::lastName)
                .set(birthDate).equalToWhenPresent(record::birthDate)
                .set(employed).equalToWhenPresent(record::employed)
                .set(occupation).equalToWhenPresent(record::occupation)
                .set(addressId).equalToWhenPresent(record::addressId)
                .where(id, isEqualTo(record::id))
                .build()
                .execute()
