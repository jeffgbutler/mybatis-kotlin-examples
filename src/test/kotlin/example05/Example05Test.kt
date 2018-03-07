package example05

import org.mybatis.dynamic.sql.SqlBuilder.*
import example05.PersonDynamicSqlSupport.Person
import example05.PersonDynamicSqlSupport.Person.id
import example05.PersonDynamicSqlSupport.Person.firstName
import example05.PersonDynamicSqlSupport.Person.employed
import example05.PersonDynamicSqlSupport.Person.occupation

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource
import org.apache.ibatis.jdbc.ScriptRunner
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.*
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.io.InputStreamReader
import java.sql.DriverManager

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.mybatis.dynamic.sql.render.RenderingStrategy
import util.YesNoTypeHandler
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
    fun testSelectByExample() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.selectByExample()
                    .where(id, isEqualTo(1))
                    .or(occupation, isNull())
                    .build()
                    .execute()

            assertThat(rows.size).isEqualTo(3)
        }
    }

    @Test
    fun testSelectByExampleWithRowbounds() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rowBounds = RowBounds(2, 2)

            val rows = mapper.selectByExample(rowBounds)
                    .where(id, isEqualTo(1))
                    .or(occupation, isNull())
                    .build()
                    .execute()

            assertThat(rows.size).isEqualTo(1)
        }
    }

    @Test
    fun testSelectDistinctByExample() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.selectDistinctByExample()
                    .where(id, isGreaterThan(1))
                    .or(occupation, isNull())
                    .build()
                    .execute()

            assertThat(rows.size).isEqualTo(5)
        }
    }

    @Test
    fun testSelectDistinctByExampleWithRowbounds() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rowBounds = RowBounds(2, 2)

            val rows = mapper.selectDistinctByExample(rowBounds)
                    .where(id, isGreaterThan(1))
                    .or(occupation, isNull())
                    .build()
                    .execute()

            assertThat(rows.size).isEqualTo(2)
        }
    }

    @Test
    fun testSelectByExampleWithTypeHandler() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.selectByExample()
                    .where(employed, isEqualTo(false))
                    .orderBy(id)
                    .build()
                    .execute()

            SoftAssertions.assertSoftly { softly ->
                softly.assertThat(rows.size).isEqualTo(2)
                softly.assertThat(rows[0].id).isEqualTo(3)
                softly.assertThat(rows[1].id).isEqualTo(6)
            }
        }
    }

    @Test
    fun testFirstNameIn() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.selectByExample()
                    .where(firstName, isIn("Fred", "Barney"))
                    .build()
                    .execute()

            assertThat(rows.size).isEqualTo(2)
            assertThat(rows[0].lastName).isEqualTo("Flintstone")
            assertThat(rows[1].lastName).isEqualTo("Rubble")
        }
    }

    @Test
    fun testDeleteByExample() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.deleteByExample()
                    .where(occupation, isNull())
                    .build()
                    .execute()
            assertThat(rows).isEqualTo(2)
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

            SoftAssertions.assertSoftly { softly ->
                var rows = mapper.insert(record)
                softly.assertThat(rows).isEqualTo(1)

                val updateRecord = record.copy(occupation = "Programmer")
                rows = mapper.updateByPrimaryKey(updateRecord)
                softly.assertThat(rows).isEqualTo(1)

                val newRecord = mapper.selectByPrimaryKey(100)
                softly.assertThat(newRecord.occupation).isEqualTo("Programmer")
            }
        }
    }

    @Test
    fun testUpdateByPrimaryKeySelective() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            SoftAssertions.assertSoftly { softly ->
                var rows = mapper.insert(record)
                softly.assertThat(rows).isEqualTo(1)

                val updateRecord = PersonRecordForUpdate(id = 100, occupation = "Programmer")
                rows = mapper.updateByPrimaryKeySelective(updateRecord)
                softly.assertThat(rows).isEqualTo(1)

                val newRecord = mapper.selectByPrimaryKey(100)
                softly.assertThat(newRecord.occupation).isEqualTo("Programmer")
                softly.assertThat(newRecord.firstName).isEqualTo("Joe")
            }

        }
    }

    @Test
    fun testUpdateByPrimaryKeySelectiveWithCopy() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            SoftAssertions.assertSoftly { softly ->
                var rows = mapper.insert(record)
                softly.assertThat(rows).isEqualTo(1)

                val updateRecord = PersonRecordForUpdate(record)
                updateRecord.occupation = "Programmer"
                rows = mapper.updateByPrimaryKeySelective(updateRecord)
                softly.assertThat(rows).isEqualTo(1)

                val newRecord = mapper.selectByPrimaryKey(100)
                softly.assertThat(newRecord.occupation).isEqualTo("Programmer")
                softly.assertThat(newRecord.firstName).isEqualTo("Joe")
            }

        }
    }

    @Test
    fun testUpdateWithNulls() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            SoftAssertions.assertSoftly { softly ->
                var rows = mapper.insert(record)
                softly.assertThat(rows).isEqualTo(1)

                val updateStatement = update(Person)
                        .set(occupation).equalToNull()
                        .set(employed).equalTo(false)
                        .where(id, isEqualTo(100))
                        .build()
                        .render(RenderingStrategy.MYBATIS3)

                rows = mapper.update(updateStatement)
                softly.assertThat(rows).isEqualTo(1)

                val newRecord = mapper.selectByPrimaryKey(100)
                softly.assertThat(newRecord.occupation).isNull()
                softly.assertThat(newRecord.employed).isEqualTo(false)
                softly.assertThat(newRecord.firstName).isEqualTo("Joe")
            }
        }
    }

    @Test
    fun testUpdateByExample() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", Date(), true, "Developer", 22)

            SoftAssertions.assertSoftly { softly ->
                var rows = mapper.insert(record)
                softly.assertThat(rows).isEqualTo(1)

                val updateRecord = record.copy(occupation = "Programmer")
                rows = mapper.updateByExample(updateRecord)
                        .where(id, isEqualTo(100))
                        .and(firstName, isEqualTo("Joe"))
                        .build()
                        .execute()

                softly.assertThat(rows).isEqualTo(1)

                val newRecord = mapper.selectByPrimaryKey(100)
                softly.assertThat(newRecord.occupation).isEqualTo("Programmer")
            }
        }
    }

    @Test
    fun testCountByExample() {
        sqlSessionFactory.openSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.countByExample()
                    .where(occupation, isNull())
                    .build()
                    .execute()

            assertThat(rows).isEqualTo(2L)
        }
    }

    companion object {
        const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
