package example06

import example06.GeneratedAlwaysDynamicSqlSupport.firstName
import example06.GeneratedAlwaysDynamicSqlSupport.generatedAlways
import example06.GeneratedAlwaysDynamicSqlSupport.id
import example06.GeneratedAlwaysDynamicSqlSupport.lastName
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
import org.mybatis.dynamic.sql.util.kotlin.elements.isEqualTo
import org.mybatis.dynamic.sql.util.kotlin.elements.isGreaterThan
import org.mybatis.dynamic.sql.util.kotlin.elements.isIn
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.insertInto
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper
import java.io.InputStreamReader
import java.sql.DriverManager

internal class Example06Test {

    private fun openSession(executorType: ExecutorType = ExecutorType.REUSE): SqlSession {
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
        config.addMapper(GeneratedAlwaysMapper::class.java)
        config.addMapper(CommonSelectMapper::class.java)
        return SqlSessionFactoryBuilder().build(config).openSession(executorType)
    }

    @Test
    fun testSelect() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val rows = mapper.select {
                where(id, isEqualTo(22))
                orderBy(id)
            }

            assertThat(rows.size).isEqualTo(1)
            assertThat(rows[0].id).isEqualTo(22)
            assertThat(rows[0].firstName).isEqualTo("Fred")
            assertThat(rows[0].lastName).isEqualTo("Flintstone")
            assertThat(rows[0].fullName).isEqualTo("Fred Flintstone")
        }
    }

    @Test
    fun testSelectAllRows() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val rows = mapper.select { allRows() }

            assertThat(rows.size).isEqualTo(4)
        }
    }

    @Test
    fun testSelectAllRowsWithOrder() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val rows = mapper.select {
                allRows()
                orderBy(firstName, lastName)
            }

            assertThat(rows.size).isEqualTo(4)
            assertThat(rows[0].firstName).isEqualTo("Barney")
        }
    }

    @Test
    fun testSelectByPrimaryKeyNoRecord() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val row = mapper.selectByPrimaryKey(106)

            assertThat(row).isNull()
        }
    }

    @Test
    fun testSelectDistinct() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val rows = mapper.selectDistinct {
                where(id, isGreaterThan(1))
            }

            assertThat(rows.size).isEqualTo(4)
        }
    }

    @Test
    fun testFirstNameIn() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val rows = mapper.select {
                where(firstName, isIn("Fred", "Barney"))
                orderBy(lastName)
            }

            assertThat(rows.size).isEqualTo(2)
            assertThat(rows[0].lastName).isEqualTo("Flintstone")
            assertThat(rows[1].lastName).isEqualTo("Rubble")
        }
    }

    @Test
    fun testDelete() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val rows = mapper.delete {
                where(lastName, isEqualTo("Rubble"))
            }
            assertThat(rows).isEqualTo(2)
        }
    }

    @Test
    fun testDeleteAllRows() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val rows = mapper.delete { allRows() }
            assertThat(rows).isEqualTo(4)
        }
    }

    @Test
    fun testDeleteByPrimaryKey() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val rows = mapper.deleteByPrimaryKey(22)

            assertThat(rows).isEqualTo(1)
        }
    }

    @Test
    fun testInsert() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val row = GeneratedAlwaysRow(firstName = "Joe", lastName = "Jones")

            val rowsInserted = mapper.insert(row)
            assertThat(rowsInserted).isEqualTo(1)
            assertThat(row.id).isEqualTo(26)
            assertThat(row.fullName).isEqualTo("Joe Jones")
        }
    }

    @Test
    fun testInsertBatch() {
        openSession(ExecutorType.BATCH).use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val row1 = GeneratedAlwaysRow(firstName = "Joe", lastName = "Jones")
            val row2 = GeneratedAlwaysRow(firstName = "Sam", lastName = "Smith")

            mapper.insertBatch(row1, row2)
            val batchResults = mapper.flush()

            assertThat(batchResults).hasSize(1)
            assertThat(batchResults.flatMap { it.updateCounts.asList() }.sum()).isEqualTo(2)
            assertThat(row1.id).isEqualTo(26)
            assertThat(row1.fullName).isEqualTo("Joe Jones")
            assertThat(row2.id).isEqualTo(27)
            assertThat(row2.fullName).isEqualTo("Sam Smith")
        }
    }

    @Test
    fun testInsertMultiple() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val row1 = GeneratedAlwaysRow(firstName = "Joe", lastName = "Jones")
            val row2 = GeneratedAlwaysRow(firstName = "Sam", lastName = "Smith")

            val rowsInserted = mapper.insertMultiple(row1, row2)
            assertThat(rowsInserted).isEqualTo(2)
            assertThat(row1.id).isEqualTo(26)
            assertThat(row1.fullName).isEqualTo("Joe Jones")
            assertThat(row2.id).isEqualTo(27)
            assertThat(row2.fullName).isEqualTo("Sam Smith")
        }
    }

    @Test
    fun testInsertGeneral() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val insertStatement = insertInto(generatedAlways) {
                set(firstName).toValue("Sam")
                set(lastName).toValue("Smith")
            }

            val rowsInserted = mapper.generalInsert(insertStatement)
            assertThat(rowsInserted).isEqualTo(1)
            assertThat(insertStatement.parameters).containsEntry("id", 26)
            assertThat(insertStatement.parameters).containsEntry("fullName", "Sam Smith")
        }
    }

    @Test
    fun testUpdateByPrimaryKey() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val row = GeneratedAlwaysRow(firstName = "Joe", lastName = "Jones")

            var rows = mapper.insert(row)
            assertThat(rows).isEqualTo(1)

            val updateRecord = row.copy(lastName = "Smith")
            rows = mapper.updateByPrimaryKey(updateRecord)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(26)
            assertThat(newRecord?.fullName).isEqualTo("Joe Smith")
        }
    }

    @Test
    fun testUpdateByPrimaryKeySelective() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val row = GeneratedAlwaysRow(firstName = "Joe", lastName = "Jones")

            var rows = mapper.insert(row)
            assertThat(rows).isEqualTo(1)

            val updateRecord = GeneratedAlwaysRow(id = 26, firstName = "Sam")
            rows = mapper.updateByPrimaryKeySelective(updateRecord)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(26)
            assertThat(newRecord?.fullName).isEqualTo("Sam Jones")
        }
    }

    @Test
    fun testUpdateByPrimaryKeySelectiveWithCopy() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val row = GeneratedAlwaysRow(firstName = "Joe", lastName = "Jones")

            var rows = mapper.insert(row)
            assertThat(rows).isEqualTo(1)

            val updateRecord = row.copy(lastName = "Smith")
            rows = mapper.updateByPrimaryKeySelective(updateRecord)
            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(26)
            assertThat(newRecord?.fullName).isEqualTo("Joe Smith")
        }
    }

    @Test
    fun testUpdate() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val row = GeneratedAlwaysRow(firstName = "Joe", lastName = "Jones")

            var rows = mapper.insert(row)
            assertThat(rows).isEqualTo(1)

            val updateRecord = row.copy(firstName = "Sam")
            rows = mapper.update {
                updateAllColumns(updateRecord)
                where(id, isEqualTo(26))
                and(firstName, isEqualTo("Joe"))
            }

            assertThat(rows).isEqualTo(1)

            val newRecord = mapper.selectByPrimaryKey(26)
            assertThat(newRecord?.fullName).isEqualTo("Sam Jones")
        }
    }

    @Test
    fun testUpdateAllRows() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)

            val updateRecord = GeneratedAlwaysRow(lastName = "Blackwell")

            val rows = mapper.update {
                updateSelectiveColumns(updateRecord)
            }

            assertThat(rows).isEqualTo(4)

            val newRecord = mapper.selectByPrimaryKey(22)
            assertThat(newRecord?.fullName).isEqualTo("Fred Blackwell")
        }
    }

    @Test
    fun testCount() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val rows = mapper.count {
                where(lastName, isEqualTo("Rubble"))
            }

            assertThat(rows).isEqualTo(2L)
        }
    }

    @Test
    fun testCountAll() {
        openSession().use { session ->
            val mapper = session.getMapper(GeneratedAlwaysMapper::class.java)
            val rows = mapper.count { allRows() }

            assertThat(rows).isEqualTo(4L)
        }
    }

    companion object {
        const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
