package example05

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
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.mybatis.dynamic.sql.exception.NonRenderingWhereClauseException
import org.mybatis.dynamic.sql.util.kotlin.elements.isLikeCaseInsensitiveWhenPresent
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper
import util.YesNoTypeHandler
import java.io.InputStreamReader
import java.sql.DriverManager

class StatementConfigurationTest {
    private fun newSession(executorType: ExecutorType = ExecutorType.REUSE): SqlSession {
        Class.forName(Example05Test.JDBC_DRIVER)
        val script = javaClass.getResourceAsStream("/CreateSimpleDB.sql")
        DriverManager.getConnection(Example05Test.JDBC_URL, "sa", "").use { connection ->
            val sr = ScriptRunner(connection)
            sr.setLogWriter(null)
            sr.runScript(InputStreamReader(script!!))
        }

        val ds = UnpooledDataSource(Example05Test.JDBC_DRIVER, Example05Test.JDBC_URL, "sa", "")
        val environment = Environment("test", JdbcTransactionFactory(), ds)
        val config = Configuration(environment)
        config.typeHandlerRegistry.register(YesNoTypeHandler::class.java)
        config.addMapper(PersonMapper::class.java)
        config.addMapper(CommonSelectMapper::class.java)
        return SqlSessionFactoryBuilder().build(config).openSession(executorType)
    }

    @Test
    fun testSelectAllRows() {
        val rows = search(null, null, true)
        assertThat(rows.size).isEqualTo(6)
    }

    @Test
    fun testSelectSomeRowsByFirstName() {
        val rows = search("fr", null, false)
        assertThat(rows.size).isEqualTo(1)
    }

    @Test
    fun testSelectSomeRowsByLastName() {
        val rows = search(null,"fl", false)
        assertThat(rows).hasSize(3)
        assertThat(rows.all { (it.lastName!!).startsWith("Ru") })
    }

    @Test
    fun testSearchCriteriaRequired() {
        assertThatExceptionOfType(NonRenderingWhereClauseException::class.java).isThrownBy {
            search(null, null, false)
        }
    }

    private fun search(firstName: String?, lastName: String?, allowSearchAll: Boolean): List<PersonRecord> {
        fun String.addWildcards() = "%$this%"

        newSession().use { session ->
            val mapper = session.getMapper(PersonMapper::class.java)

            return mapper.select {
                where {
                    person.firstName (isLikeCaseInsensitiveWhenPresent(firstName)
                        .filter{ it.isNotEmpty() }
                        .map { it.addWildcards() })
                    and {
                        person.lastName (isLikeCaseInsensitiveWhenPresent(lastName)
                                .filter{ it.isNotEmpty() }
                                .map { it.addWildcards()})
                    }
                }
                configureStatement { isNonRenderingWhereClauseAllowed = allowSearchAll }
            }
        }
    }
}
