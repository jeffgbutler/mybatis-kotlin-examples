package example08

import java.io.InputStreamReader
import java.sql.DriverManager
import java.time.LocalDate

import org.assertj.core.api.Assertions.assertThat
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource
import org.apache.ibatis.jdbc.ScriptRunner
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.junit.jupiter.api.Test

class Example08Test {
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
        config.addMapper(Example08Mapper::class.java)
        return SqlSessionFactoryBuilder().build(config).openSession()
    }

    @Test
    fun selectAddressById() {
        newSession().use { session ->
            val mapper = session.getMapper(Example08Mapper::class.java)

            val address = mapper.selectAddressById(1)

            assertThat(address.id).isEqualTo(1)
            assertThat(address.streetAddress).isEqualTo("123 Main Street")
            assertThat(address.city).isEqualTo("Bedrock")
            assertThat(address.state).isEqualTo("IN")

            assertThat(address.people.size).isEqualTo(3)

            assertThat(address.people[0].firstName).isEqualTo("Fred")
            assertThat(address.people[0].lastName).isEqualTo("Flintstone")
            assertThat(address.people[0].birthDate).isEqualTo(LocalDate.of(1935, 2, 1))
            assertThat(address.people[0].employed).isTrue
            assertThat(address.people[0].occupation).isEqualTo("Brontosaurus Operator")

            assertThat(address.people[1].firstName).isEqualTo("Wilma")
            assertThat(address.people[1].lastName).isEqualTo("Flintstone")
            assertThat(address.people[1].birthDate).isEqualTo(LocalDate.of(1940, 2, 1))
            assertThat(address.people[1].employed).isTrue
            assertThat(address.people[1].occupation).isEqualTo("Accountant")

            assertThat(address.people[2].firstName).isEqualTo("Pebbles")
            assertThat(address.people[2].lastName).isEqualTo("Flintstone")
            assertThat(address.people[2].birthDate).isEqualTo(LocalDate.of(1960, 5, 6))
            assertThat(address.people[2].employed).isFalse
            assertThat(address.people[2].occupation).isNull()
        }
    }

    @Test
    fun selectPersonWithAllFields() {
        newSession().use { session ->
            val mapper = session.getMapper(Example08Mapper::class.java)

            val person = mapper.selectPersonById(1)

            assertThat(person.id).isEqualTo(1)
            assertThat(person.firstName).isEqualTo("Fred")
            assertThat(person.lastName).isEqualTo("Flintstone")
            assertThat(person.birthDate).isEqualTo(LocalDate.of(1935, 2, 1))
            assertThat(person.employed).isTrue
            assertThat(person.occupation).isEqualTo("Brontosaurus Operator")
            assertThat(person.address?.id).isEqualTo(1)
            assertThat(person.address?.streetAddress).isEqualTo("123 Main Street")
            assertThat(person.address?.city).isEqualTo("Bedrock")
            assertThat(person.address?.state).isEqualTo("IN")
        }
    }

    @Test
    fun selectPersonWithNullOccupation() {
        newSession().use { session ->
            val mapper = session.getMapper(Example08Mapper::class.java)

            val person = mapper.selectPersonById(3)

            assertThat(person.id).isEqualTo(3)
            assertThat(person.firstName).isEqualTo("Pebbles")
            assertThat(person.lastName).isEqualTo("Flintstone")
            assertThat(person.birthDate).isEqualTo(LocalDate.of(1960, 5, 6))
            assertThat(person.employed).isFalse
            assertThat(person.occupation).isNull()
            assertThat(person.address?.id).isEqualTo(1)
            assertThat(person.address?.streetAddress).isEqualTo("123 Main Street")
            assertThat(person.address?.city).isEqualTo("Bedrock")
            assertThat(person.address?.state).isEqualTo("IN")
        }
    }

    companion object {
        const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
