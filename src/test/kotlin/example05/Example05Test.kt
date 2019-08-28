package example05

import example05.PersonDynamicSqlSupport.Person.employed
import example05.PersonDynamicSqlSupport.Person.firstName
import example05.PersonDynamicSqlSupport.Person.id
import example05.PersonDynamicSqlSupport.Person.lastName
import example05.PersonDynamicSqlSupport.Person.occupation
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource
import org.apache.ibatis.jdbc.ScriptRunner
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.dynamic.sql.SqlBuilder.*
import org.mybatis.dynamic.sql.util.kotlin.allRows
import org.mybatis.dynamic.sql.util.kotlin.and
import org.mybatis.dynamic.sql.util.kotlin.or
import util.YesNoTypeHandler
import java.io.InputStreamReader
import java.sql.DriverManager
import java.util.*

internal class Example05Test {

    private fun openSession(): SqlSession {
        Class.forName(JDBC_DRIVER)
        val script = javaClass.getResourceAsStream("/CreateSimpleDB.sql")
        DriverManager.getConnection(JDBC_URL, "sa", "").use { connection ->
            val sr = ScriptRunner(connection)
            sr.setLogWriter(null)
            sr.runScript(InputStreamReader(script))
        }

        val ds = UnpooledDataSource(JDBC_DRIVER, JDBC_URL, "sa", "")
        val environment = Environment("test", JdbcTransactionFactory(), ds)
        val config = Configuration(environment)
        config.typeHandlerRegistry.register(YesNoTypeHandler::class.java)
        config.addMapper(PersonMapper::class.java)
        return SqlSessionFactoryBuilder().build(config).openSession()
    }

    @Test
    fun testSelect() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select {
                where(id, isEqualTo(1))
                or(occupation, isNull())
                orderBy(id)
            }

            assertThat(rows.size).isEqualTo(3)
            assertThat(rows[0].id).isEqualTo(1)
            assertThat(rows[0].firstName).isEqualTo("Fred")
            assertThat(rows[0].lastName).isEqualTo("Flintstone")
            assertThat(rows[0].birthDate).isNotNull()
            assertThat(rows[0].employed).isTrue()
            assertThat(rows[0].occupation).isEqualTo("Brontosaurus Operator")
            assertThat(rows[0].addressId).isEqualTo(1)
        }
    }

    @Test
    fun testSelectAllRows() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select { allRows() }

            assertThat(rows.size).isEqualTo(6)
        }
    }

    @Test
    fun testSelectAllRowsWithOrder() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select {
                allRows()
                orderBy(firstName, lastName)
            }

            assertThat(rows.size).isEqualTo(6)
            assertThat(rows[0].firstName).isEqualTo("Bamm Bamm")
        }
    }

    @Test
    fun testSelectByPrimaryKeyNoRecord() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val row = mapper.selectByPrimaryKey(22)

            assertThat(row?.id).isNull()
            assertThat(row).isNull()
        }
    }

    @Test
    fun testSelectDistinct() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.selectDistinct {
                where(id, isGreaterThan(1))
                or(occupation, isNull())
            }

            assertThat(rows.size).isEqualTo(5)
        }
    }

    @Test
    fun testSelectWithTypeHandler() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select {
                where(employed, isEqualTo(false))
                orderBy(id)
            }

            assertThat(rows.size).isEqualTo(2)
            assertThat(rows[0].id).isEqualTo(3)
            assertThat(rows[1].id).isEqualTo(6)
        }
    }

    @Test
    fun testFirstNameIn() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select { where(firstName, isIn("Fred", "Barney")) }

            assertThat(rows.size).isEqualTo(2)
            assertThat(rows[0].lastName).isEqualTo("Flintstone")
            assertThat(rows[1].lastName).isEqualTo("Rubble")
        }
    }

    @Test
    fun testDelete() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.delete { where(occupation, isNull()) }
            assertThat(rows).isEqualTo(2)
        }
    }

    @Test
    fun testDeleteAllRows() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.delete { allRows() }
            assertThat(rows).isEqualTo(6)
        }
    }

    @Test
    fun testDeleteByPrimaryKey() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.deleteByPrimaryKey(2)

            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testInsert() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(10, "Joe", "Jones", Date(), true, "Developer", 22)

            val rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testInsertMultiple() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.insertMultiple(
                    PersonRecord(10, "Joe", "Jones", Date(), true, "Developer", 22),
                    PersonRecord(11, "Sam", "Smith", Date(), true, "Architect", 23))
            assertThat(rows).isEqualTo(2)
        }
    }

    @Test
    fun testInsertWithNull() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), false, null, 22)

            val rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testUpdateByPrimaryKey() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = record.copy(occupation = "Programmer")
            rows = mapper.updateByPrimaryKey(updateRecord)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
        }
    }

    @Test
    fun testUpdateByPrimaryKeySelective() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = PersonRecord(id = 100, occupation = "Programmer")
            rows = mapper.updateByPrimaryKeySelective(updateRecord)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
            assertThat(newRecord?.firstName).isEqualTo("Joe")

        }
    }

    @Test
    fun testUpdateByPrimaryKeySelectiveWithCopy() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = PersonRecord(record.id, record.firstName, record.lastName, record.birthDate,
                    record.employed, "Programmer", record.addressId)
            rows = mapper.updateByPrimaryKeySelective(updateRecord)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
            assertThat(newRecord?.firstName).isEqualTo("Joe")
        }
    }

    @Test
    fun testUpdateWithNulls() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            rows = mapper.update {
                set(occupation).equalToNull()
                set(employed).equalTo(false)
                where(id, isEqualTo(100))
            }

            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isNull()
            assertThat(newRecord?.employed).isEqualTo(false)
            assertThat(newRecord?.firstName).isEqualTo("Joe")
        }
    }

    @Test
    fun testUpdate() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = record.copy(occupation = "Programmer")
            rows = mapper.update {
                updateAllColumns(updateRecord)
                where(id, isEqualTo(100))
                and(firstName, isEqualTo("Joe"))
            }

            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
        }
    }

    @Test
    fun testUpdateAllRows() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = PersonRecord(occupation = "Programmer")

            rows = mapper.update {
                updateSelectiveColumns(updateRecord)
            }

            assertThat(rows).isEqualTo(7)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
        }
    }

    @Test
    fun testCount() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.count { where(occupation, isNull()) }

            assertThat(rows).isEqualTo(2L)
        }
    }

    @Test
    fun testCountAll() {
        openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.count { allRows() }

            assertThat(rows).isEqualTo(6L)
        }
    }

    companion object {
        const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
