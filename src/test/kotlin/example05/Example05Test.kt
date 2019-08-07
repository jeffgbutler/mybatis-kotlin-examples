package example05

import example05.PersonDynamicSqlSupport.Person
import example05.PersonDynamicSqlSupport.Person.employed
import example05.PersonDynamicSqlSupport.Person.firstName
import example05.PersonDynamicSqlSupport.Person.id
import example05.PersonDynamicSqlSupport.Person.lastName
import example05.PersonDynamicSqlSupport.Person.occupation
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource
import org.apache.ibatis.jdbc.ScriptRunner
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mybatis.dynamic.sql.SqlBuilder.*
import org.mybatis.dynamic.sql.kotlin.*
import org.mybatis.dynamic.sql.render.RenderingStrategy
import util.YesNoTypeHandler
import java.io.InputStreamReader
import java.sql.DriverManager
import java.util.*

internal class Example05Test {

    private lateinit var sqlSessionFactory: SqlSessionFactory

    @BeforeEach
    fun setup() {
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
        sqlSessionFactory = SqlSessionFactoryBuilder().build(config)
    }

    @Test
    fun testSelect() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select {
                where(id, isEqualTo(1))
                        .or(occupation, isNull())
                        .orderBy(id)
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
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select(selectAllRows())

            assertThat(rows.size).isEqualTo(6)
        }
    }

    @Test
    fun testSelectAllRowsWithOrder() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select(selectAllRowsOrderedBy(firstName, lastName))

            assertThat(rows.size).isEqualTo(6)
            assertThat(rows[0].firstName).isEqualTo("Bamm Bamm")
        }
    }

    @Test
    fun testSelectByPrimaryKeyNoRecord() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val row = mapper.selectByPrimaryKey(22)

            assertThat(row?.id).isNull()
            assertThat(row).isNull()
        }
    }

    @Test
    fun testSelectDistinct() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.selectDistinct {
                where(id, isGreaterThan(1))
                        .or(occupation, isNull())
            }

            assertThat(rows.size).isEqualTo(5)
        }
    }

    @Test
    fun testSelectWithTypeHandler() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select {
                where(employed, isEqualTo(false))
                        .orderBy(id)
            }

            assertThat(rows.size).isEqualTo(2)
            assertThat(rows[0].id).isEqualTo(3)
            assertThat(rows[1].id).isEqualTo(6)
        }
    }

    @Test
    fun testFirstNameIn() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select { where(firstName, isIn("Fred", "Barney")) }

            assertThat(rows.size).isEqualTo(2)
            assertThat(rows[0].lastName).isEqualTo("Flintstone")
            assertThat(rows[1].lastName).isEqualTo("Rubble")
        }
    }

    @Test
    fun testDelete() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.delete { where(occupation, isNull()) }
            assertThat(rows).isEqualTo(2)
        }
    }

    @Test
    fun testDeleteAllRows() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.delete(deleteAllRows())
            assertThat(rows).isEqualTo(6)
        }
    }

    @Test
    fun testDeleteByPrimaryKey() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.deleteByPrimaryKey(2)

            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testInsert() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(10, "Joe", "Jones", Date(), true, "Developer", 22)

            val rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testInsertMultiple() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.insertMultiple(
                    PersonRecord(10, "Joe", "Jones", Date(), true, "Developer", 22),
                    PersonRecord(11, "Sam", "Smith", Date(), true, "Architect", 23))
            assertThat(rows).isEqualTo(2)
        }
    }

    @Test
    fun testInsertWithNull() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), false, null, 22)

            val rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testUpdateByPrimaryKey() {
        sqlSessionFactory.openSession().use { session ->
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
        sqlSessionFactory.openSession().use { session ->
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
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = PersonRecord(record.id, record.firstName, record.lastName, record.birthDate, record.employed, "Programmer", record.addressId)
            rows = mapper.updateByPrimaryKeySelective(updateRecord)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
            assertThat(newRecord?.firstName).isEqualTo("Joe")
        }
    }

    @Test
    fun testUpdateWithNulls() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateStatement = update(Person)
                    .set(occupation).equalToNull()
                    .set(employed).equalTo(false)
                    .where(id, isEqualTo(100))
                    .build()
                    .render(RenderingStrategy.MYBATIS3)

            rows = mapper.update(updateStatement)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isNull()
            assertThat(newRecord?.employed).isEqualTo(false)
            assertThat(newRecord?.firstName).isEqualTo("Joe")
        }
    }

    @Test
    fun testUpdate() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = record.copy(occupation = "Programmer")
            rows = mapper.update {
                setAll(updateRecord)
                where(id, isEqualTo(100))
                        .and(firstName, isEqualTo("Joe"))
            }

            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
        }
    }

    @Test
    fun testUpdateAllRows() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = PersonRecord(occupation = "Programmer")

            rows = mapper.update {
                setSelective(updateRecord)
            }

            assertThat(rows).isEqualTo(7)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
        }
    }

    @Test
    fun testCount() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.count { where(occupation, isNull()) }

            assertThat(rows).isEqualTo(2L)
        }
    }

    @Test
    fun testCountAll() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.count(countAllRows())

            assertThat(rows).isEqualTo(6L)
        }
    }

    companion object {
        const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
