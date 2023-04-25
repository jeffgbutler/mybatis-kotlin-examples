package example05

import example05.PersonDynamicSqlSupport.employed
import example05.PersonDynamicSqlSupport.firstName
import example05.PersonDynamicSqlSupport.id
import example05.PersonDynamicSqlSupport.lastName
import example05.PersonDynamicSqlSupport.occupation
import example05.PersonDynamicSqlSupport.parentId
import example05.PersonDynamicSqlSupport.person
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource
import org.apache.ibatis.jdbc.ScriptRunner
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.ExecutorType
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.select
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper
import util.YesNoTypeHandler
import util.matchesAny
import java.io.InputStreamReader
import java.sql.DriverManager
import java.time.LocalDate

internal class Example05Test {

    private fun newSession(executorType: ExecutorType = ExecutorType.REUSE): SqlSession {
        Class.forName(JDBC_DRIVER)
        val script = javaClass.getResourceAsStream("/CreateSimpleDB.sql")
        DriverManager.getConnection(JDBC_URL, "sa", "").use { connection ->
            val sr = ScriptRunner(connection)
            sr.setLogWriter(null)
            sr.runScript(InputStreamReader(script!!))
        }

        val ds = UnpooledDataSource(JDBC_DRIVER, JDBC_URL, "sa", "")
        val environment = Environment("test", JdbcTransactionFactory(), ds)
        val config = Configuration(environment)
        config.typeHandlerRegistry.register(YesNoTypeHandler::class.java)
        config.addMapper(PersonMapper::class.java)
        config.addMapper(CommonSelectMapper::class.java)
        return SqlSessionFactoryBuilder().build(config).openSession(executorType)
    }

    @Test
    fun testSelect() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select {
                where {
                    id isEqualTo 1
                    or { occupation.isNull() }
                }
                orderBy(id)
            }

            assertThat(rows.size).isEqualTo(3)
            assertThat(rows[0].id).isEqualTo(1)
            assertThat(rows[0].firstName).isEqualTo("Fred")
            assertThat(rows[0].lastName).isEqualTo("Flintstone")
            assertThat(rows[0].birthDate).isNotNull
            assertThat(rows[0].employed).isTrue
            assertThat(rows[0].occupation).isEqualTo("Brontosaurus Operator")
            assertThat(rows[0].addressId).isEqualTo(1)
            assertThat(rows[0].parentId).isNull()
        }
    }

    @Test
    fun testSelectAllRows() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select { allRows() }

            assertThat(rows.size).isEqualTo(6)
        }
    }

    @Test
    fun testSelectAllRowsWithOrder() {
        newSession().use { session ->
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
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val row = mapper.selectByPrimaryKey(22)

            assertThat(row?.id).isNull()
            assertThat(row).isNull()
        }
    }

    @Test
    fun testSelectDistinct() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.selectDistinct {
                where {
                    id isGreaterThan 1
                    or { occupation.isNull() }
                }
            }

            assertThat(rows.size).isEqualTo(5)
        }
    }

    @Test
    fun testSelectWithTypeHandler() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select {
                where { employed isEqualTo false }
                orderBy(id)
            }

            assertThat(rows.size).isEqualTo(2)
            assertThat(rows[0].id).isEqualTo(3)
            assertThat(rows[1].id).isEqualTo(6)
        }
    }

    @Test
    fun testFirstNameIn() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.select { where { firstName.isIn("Fred", "Barney") } }

            assertThat(rows.size).isEqualTo(2)
            assertThat(rows[0].lastName).isEqualTo("Flintstone")
            assertThat(rows[1].lastName).isEqualTo("Rubble")
        }
    }

    @Test
    fun testDelete() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.delete { where { occupation.isNull() } }
            assertThat(rows).isEqualTo(2)
        }
    }

    @Test
    fun testDeleteAllRows() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.delete { allRows() }
            assertThat(rows).isEqualTo(6)
        }
    }

    @Test
    fun testDeleteByPrimaryKey() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.deleteByPrimaryKey(2)

            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testInsert() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(10, "Joe", "Jones", LocalDate.now(), true, "Developer", 22)

            val rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testInsertMultiple() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            val rows = mapper.insertMultiple(
                PersonRecord(10, "Joe", "Jones", LocalDate.now(), true, "Developer", 22),
                PersonRecord(11, "Sam", "Smith", LocalDate.now(), true, "Architect", 23)
            )
            assertThat(rows).isEqualTo(2)
        }
    }

    @Test
    fun testInsertBatch() {
        newSession(ExecutorType.BATCH).use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            mapper.insertBatch(
                PersonRecord(10, "Joe", "Jones", LocalDate.now(), true, "Developer", 22),
                PersonRecord(11, "Sam", "Smith", LocalDate.now(), true, "Architect", 23)
            )

            val batchResults = mapper.flush()
            assertThat(batchResults).hasSize(1)
            assertThat(batchResults.flatMap { it.updateCounts.asList() }.sum()).isEqualTo(2)
        }
    }

    @Test
    fun testInsertWithNull() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", LocalDate.now(), false, null, 22)

            val rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testUpdateByPrimaryKey() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", LocalDate.now(), true, "Developer", 22)

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
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", LocalDate.now(), true, "Developer", 22)

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
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", LocalDate.now(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = record.copy(occupation = "Programmer")
            rows = mapper.updateByPrimaryKeySelective(updateRecord)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
            assertThat(newRecord?.firstName).isEqualTo("Joe")
        }
    }

    @Test
    fun testUpdateWithNulls() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", LocalDate.now(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            rows = mapper.update {
                set(occupation).equalToNull()
                set(employed).equalTo(false)
                where { id isEqualTo 100 }
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
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", LocalDate.now(), true, "Developer", 22)

            var rows = mapper.insert(record)
            assertThat(rows).isEqualTo(1)

            val updateRecord = record.copy(occupation = "Programmer")
            rows = mapper.update {
                updateAllColumns(updateRecord)
                where {
                    id isEqualTo 100
                    and { firstName isEqualTo "Joe" }
                }
            }

            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(100)
            assertThat(newRecord?.occupation).isEqualTo("Programmer")
        }
    }

    @Test
    fun testUpdateAllRows() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val record = PersonRecord(100, "Joe", "Jones", LocalDate.now(), true, "Developer", 22)

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
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.count { where { occupation.isNull() } }

            assertThat(rows).isEqualTo(2L)
        }
    }

    @Test
    fun testCountAll() {
        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)
            val rows = mapper.count { allRows() }

            assertThat(rows).isEqualTo(6L)
        }
    }

    @Test
    fun testSelfJoin() {
        newSession().use { session ->
            val mapper = session.getMapper(CommonSelectMapper::class.java)

            val person2 = PersonDynamicSqlSupport.Person()

            // get Bamm Bamm's parent - should be Barney
            val selectStatement = select(id, firstName, parentId) {
                from(person, "p1")
                join(person2, "p2") {
                    on(id) equalTo person2.parentId
                }
                where { person2.id isEqualTo 6 }
            }

            val expectedStatement = ("select p1.id, p1.first_name, p1.parent_id"
                    + " from Person p1 join Person p2 on p1.id = p2.parent_id"
                    + " where p2.id = #{parameters.p1,jdbcType=INTEGER}")
            assertThat(selectStatement.selectStatement).isEqualTo(expectedStatement)

            val row = mapper.selectOneMappedRow(selectStatement)

            assertThat(row).isNotNull
            assertThat(row).containsEntry("ID", 4)
            assertThat(row).containsEntry("FIRST_NAME", "Barney")
            assertThat(row).doesNotContainKey("PARENT_ID")
        }
    }

    @Test
    fun testReceiverFunction() {
        newSession().use { session ->
            val mapper = session.getMapper(CommonSelectMapper::class.java)

            // find all children with a convoluted query
            val selectStatement = select(id, firstName, lastName) {
                from(person)
                where {
                    id matchesAny {
                        select (id) {
                            from(person)
                            where {
                                parentId.isNotNull()
                            }
                        }
                    }
                }
                orderBy(id)
            }

            val expected = "select id, first_name, last_name from Person " +
                    "where id = any (select id from Person where parent_id is not null) order by id"

            assertThat(selectStatement.selectStatement).isEqualTo(expected)

            val rows = mapper.selectManyMappedRows(selectStatement)

            assertThat(rows).hasSize(2)
            assertThat(rows[0]["FIRST_NAME"]).isEqualTo("Pebbles")
            assertThat(rows[1]["FIRST_NAME"]).isEqualTo("Bamm Bamm")
        }
    }

    companion object {
        const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
