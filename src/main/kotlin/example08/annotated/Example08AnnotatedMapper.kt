package example08.annotated

import example08.Address
import example08.AddressWithPeople
import example08.Person
import example08.PersonWithAddress
import org.apache.ibatis.annotations.Arg
import org.apache.ibatis.annotations.NamedResultMap
import org.apache.ibatis.annotations.ResultOrdered
import org.apache.ibatis.annotations.Select
import util.YesNoTypeHandler
import java.time.LocalDate

@NamedResultMap(id = Example08AnnotatedMapper.PERSON_RESULT_MAP_ID, javaType = Person::class, constructorArguments = [
    Arg(column = "person_id", javaType= Int::class, id = true),
    Arg(column="first_name", javaType= String::class),
    Arg(column="last_name" ,javaType= String::class),
    Arg(column="birth_date", javaType= LocalDate::class),
    Arg(column="employed", javaType= Boolean::class, typeHandler= YesNoTypeHandler::class),
    Arg(column="occupation", javaType= String::class)
])
@NamedResultMap(id = Example08AnnotatedMapper.ADDRESS_RESULT_MAP_ID, javaType = Address::class, constructorArguments = [
    Arg(column="id", javaType= Int::class, id = true),
    Arg(column="street_address", javaType= String::class),
    Arg(column="city", javaType= String::class),
    Arg(column="state", javaType= String::class)
])
interface Example08AnnotatedMapper {
    companion object {
        const val PERSON_RESULT_MAP_ID = "PersonResult"
        const val ADDRESS_RESULT_MAP_ID = "AddressResult"
    }

    @Select(
        "select a.id, a.street_address, a.city, a.state, p.id as person_id, p.first_name, p.last_name, p.birth_date, p.employed, p.occupation",
        "from Address a join Person p on a.id = p.address_id",
        "where a.id = #{value}",
        "order by id, person_id"
    )
    @Arg(column = "id", javaType = Int::class, id = true)
    @Arg(column="street_address", javaType= String::class)
    @Arg(column="city", javaType= String::class)
    @Arg(column="state", javaType= String::class)
    @Arg(resultMap=PERSON_RESULT_MAP_ID, javaType= List::class)
    @ResultOrdered
    fun selectAddressById(id: Int): AddressWithPeople

    @Select(
    "select a.id, a.street_address, a.city, a.state, p.id as person_id, p.first_name, p.last_name, p.birth_date, p.employed, p.occupation",
    "from Address a join Person p on a.id = p.address_id",
    "where p.id = #{value}"
    )
    @Arg(column="person_id", javaType=Int::class, id = true)
    @Arg(column="first_name", javaType= String::class)
    @Arg(column="last_name", javaType= String::class)
    @Arg(column="birth_date", javaType= LocalDate::class)
    @Arg(column="employed", javaType= Boolean::class, typeHandler= YesNoTypeHandler::class)
    @Arg(column="occupation", javaType= String::class)
    @Arg(resultMap=ADDRESS_RESULT_MAP_ID, javaType= Address::class)
    fun selectPersonById(id: Int): PersonWithAddress
}
