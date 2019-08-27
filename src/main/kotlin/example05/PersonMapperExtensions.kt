package example05

import example05.PersonDynamicSqlSupport.Person
import example05.PersonDynamicSqlSupport.Person.addressId
import example05.PersonDynamicSqlSupport.Person.birthDate
import example05.PersonDynamicSqlSupport.Person.employed
import example05.PersonDynamicSqlSupport.Person.firstName
import example05.PersonDynamicSqlSupport.Person.id
import example05.PersonDynamicSqlSupport.Person.lastName
import example05.PersonDynamicSqlSupport.Person.occupation
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.update.UpdateDSL
import org.mybatis.dynamic.sql.update.UpdateModel
import org.mybatis.dynamic.sql.util.kotlin.CountCompleter
import org.mybatis.dynamic.sql.util.kotlin.DeleteCompleter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insert
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insertMultiple
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils

fun PersonMapper.count(completer: CountCompleter) =
        MyBatis3Utils.count(this::count, Person, completer)

fun PersonMapper.delete(completer: DeleteCompleter) =
        MyBatis3Utils.deleteFrom(this::delete, Person, completer)

fun PersonMapper.deleteByPrimaryKey(id_: Int) =
        delete {
            where(id, isEqualTo(id_))
        }

fun PersonMapper.insert(record: PersonRecord) =
        insert(this::insert, record, Person) {
            map(id).toProperty("id")
            map(firstName).toProperty("firstName")
            map(lastName).toProperty("lastName")
            map(birthDate).toProperty("birthDate")
            map(employed).toProperty("employed")
            map(occupation).toProperty("occupation")
            map(addressId).toProperty("addressId")
        }

fun PersonMapper.insertMultiple(vararg records: PersonRecord) =
        insertMultiple(records.toList())

fun PersonMapper.insertMultiple(records: List<PersonRecord>) =
        insertMultiple(this::insertMultiple, records, Person) {
            map(id).toProperty("id")
            map(firstName).toProperty("firstName")
            map(lastName).toProperty("lastName")
            map(birthDate).toProperty("birthDate")
            map(employed).toProperty("employed")
            map(occupation).toProperty("occupation")
            map(addressId).toProperty("addressId")
        }

private val selectList = arrayOf(id.`as`("A_ID"), firstName, lastName, birthDate, employed, occupation, addressId)

fun PersonMapper.selectOne(completer: SelectCompleter) =
        MyBatis3Utils.selectOne(this::selectOne, selectList, Person, completer)

fun PersonMapper.select(completer: SelectCompleter): List<PersonRecord> =
        MyBatis3Utils.selectList(this::selectMany, selectList, Person, completer)

fun PersonMapper.selectDistinct(completer: SelectCompleter): List<PersonRecord> =
        MyBatis3Utils.selectDistinct(this::selectMany, selectList, Person, completer)

fun PersonMapper.selectByPrimaryKey(id_: Int) =
        selectOne {
            where(id, isEqualTo(id_))
        }

fun PersonMapper.update(completer: UpdateCompleter) =
        MyBatis3Utils.update(this::update, Person, completer)

fun PersonMapper.updateByPrimaryKey(record: PersonRecord) =
        update {
            set(firstName).equalTo(record::firstName)
            set(lastName).equalTo(record::lastName)
            set(birthDate).equalTo(record::birthDate)
            set(employed).equalTo(record::employed)
            set(occupation).equalTo(record::occupation)
            set(addressId).equalTo(record::addressId)
            where(id, isEqualTo(record::id))
        }

fun PersonMapper.updateByPrimaryKeySelective(record: PersonRecord) =
        update {
            set(firstName).equalToWhenPresent(record::firstName)
            set(lastName).equalToWhenPresent(record::lastName)
            set(birthDate).equalToWhenPresent(record::birthDate)
            set(employed).equalToWhenPresent(record::employed)
            set(occupation).equalToWhenPresent(record::occupation)
            set(addressId).equalToWhenPresent(record::addressId)
            where(id, isEqualTo(record::id))
        }

fun UpdateDSL<UpdateModel>.setAll(record: PersonRecord) =
        apply {
            set(id).equalTo(record::id)
            set(firstName).equalTo(record::firstName)
            set(lastName).equalTo(record::lastName)
            set(birthDate).equalTo(record::birthDate)
            set(employed).equalTo(record::employed)
            set(occupation).equalTo(record::occupation)
            set(addressId).equalTo(record::addressId)
        }

fun UpdateDSL<UpdateModel>.setSelective(record: PersonRecord) =
        apply {
            set(id).equalToWhenPresent(record::id)
            set(firstName).equalToWhenPresent(record::firstName)
            set(lastName).equalToWhenPresent(record::lastName)
            set(birthDate).equalToWhenPresent(record::birthDate)
            set(employed).equalToWhenPresent(record::employed)
            set(occupation).equalToWhenPresent(record::occupation)
            set(addressId).equalToWhenPresent(record::addressId)
        }
