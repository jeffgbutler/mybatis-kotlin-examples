package example03

import org.assertj.core.api.Assertions.*
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource
import org.apache.ibatis.jdbc.ScriptRunner
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.junit.jupiter.api.Test
import java.io.InputStreamReader
import java.sql.DriverManager
import java.time.LocalDate

class Example03Test {
    private fun newSession(): SqlSession {
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
        config.addMapper(Example03Mapper::class.java)
        return SqlSessionFactoryBuilder().build(config).openSession()
    }

    @Test
    fun selectPersonWithAllFields() {
        newSession().use { session ->
            val mapper = session.getMapper(Example03Mapper::class.java)

            val person = mapper.selectPersonById(1)

            assertThat(person.id).isEqualTo(1)
            assertThat(person.firstName).isEqualTo("Fred")
            assertThat(person.lastName).isEqualTo("Flintstone")
            assertThat(person.birthDate).isEqualTo(LocalDate.of(1935, 2, 1))
            assertThat(person.employed).isTrue
            assertThat(person.occupation).isEqualTo("Brontosaurus Operator")
            assertThat(person.address.id).isEqualTo(1)
            assertThat(person.address.streetAddress).isEqualTo("123 Main Street")
            assertThat(person.address.city).isEqualTo("Bedrock")
            assertThat(person.address.state).isEqualTo("IN")
        }
    }

    @Test
    fun selectPersonWithNullOccupation() {
        newSession().use { session ->
            val mapper = session.getMapper(Example03Mapper::class.java)

            val person = mapper.selectPersonById(3)

            assertThat(person.id).isEqualTo(3)
            assertThat(person.firstName).isEqualTo("Pebbles")
            assertThat(person.lastName).isEqualTo("Flintstone")
            assertThat(person.birthDate).isEqualTo(LocalDate.of(1960, 5, 6))
            assertThat(person.employed).isFalse
            assertThat(person.occupation).isNull()
            assertThat(person.address.id).isEqualTo(1)
            assertThat(person.address.streetAddress).isEqualTo("123 Main Street")
            assertThat(person.address.city).isEqualTo("Bedrock")
            assertThat(person.address.state).isEqualTo("IN")
        }
    }

    @Test
    fun selectPersonWithNullOccupationAndElvisOperator() {
        newSession().use { session ->
            val mapper = session.getMapper(Example03Mapper::class.java)

            val person = mapper.selectPersonById(3)

            assertThat(person.id).isEqualTo(3)
            assertThat(person.firstName).isEqualTo("Pebbles")
            assertThat(person.lastName).isEqualTo("Flintstone")
            assertThat(person.birthDate).isEqualTo(LocalDate.of(1960, 5, 6))
            assertThat(person.employed).isFalse
            assertThat(person.occupation ?: "<null>").isEqualTo("<null>")
            assertThat(person.address.id).isEqualTo(1)
            assertThat(person.address.streetAddress).isEqualTo("123 Main Street")
            assertThat(person.address.city).isEqualTo("Bedrock")
            assertThat(person.address.state).isEqualTo("IN")
        }
    }

    companion object {
        const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
