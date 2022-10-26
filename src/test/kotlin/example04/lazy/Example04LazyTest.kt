package example04.lazy

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

class Example04LazyTest {
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
        config.addMapper(Example04LazyMapper::class.java)
        return SqlSessionFactoryBuilder().build(config).openSession()
    }

    @Test
    fun selectAddressByIdForcingLazyLoad() {
        newSession().use { session ->
            val mapper = session.getMapper(Example04LazyMapper::class.java)

            val address = mapper.selectAddressById(1)

            assertThat(address.id).isEqualTo(1)
            assertThat(address.streetAddress).isEqualTo("123 Main Street")
            assertThat(address.city).isEqualTo("Bedrock")
            assertThat(address.state).isEqualTo("IN")

            // This call forces the lazy load to trigger. MyBatis uses Javassist to proxy objects when lazy loading
            // is enabled. Javassist currently doesn't support Kotlin. Calling "hashCode" (or "toString",
            // "equals", or "clone") will cause the lazy load to trigger (these 4 methods are configured in
            // MyBatis by default)
            address.hashCode()

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
    fun selectAddressByIdDemonstratingLazyLoadFailure() {
        newSession().use { session ->
            val mapper = session.getMapper(Example04LazyMapper::class.java)

            val address = mapper.selectAddressById(1)

            assertThat(address.id).isEqualTo(1)
            assertThat(address.streetAddress).isEqualTo("123 Main Street")
            assertThat(address.city).isEqualTo("Bedrock")
            assertThat(address.state).isEqualTo("IN")

            // this exception will be thrown because the lazy load is not triggered due to
            // Javassist not supporting Kotlin
            assertThatExceptionOfType(UninitializedPropertyAccessException::class.java).isThrownBy {
                assertThat(address.people.size).isEqualTo(3)
            }
        }
    }

    companion object {
        const val JDBC_URL = "jdbc:hsqldb:mem:aname"
        const val JDBC_DRIVER = "org.hsqldb.jdbcDriver"
    }
}
