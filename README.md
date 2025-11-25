# MyBatis Kotlin Examples

This project demonstrates the use of MyBatis with the Kotlin language.  This is a work in progress, and we will update as
our learnings progress and/or new capabilities in MyBatis become available.

Note that in most cases we are foregoing XML and working solely with the Java API for MyBatis.  Hopefully, it will be
clear how to translate the concepts to XML if so desired.

All the code in this project is pure Kotlin. As is typical, source code is in the /src/main/kotlin
directory and test code is in /src/test/kotlin.  The examples described below have associated packages
in the source trees.  So the code for example01 is in /src/main/kotlin/example01, etc.  

## Using MyBatis with Kotlin

One of MyBatis' core strengths is mapping SQL result sets to an object graph.  MyBatis is good at these mappings for
many types of class hierarchies. MyBatis can create a deeply nested object graph, and its group by function
provides support for creating an object graph that avoids the need for N+1 queries. However, the implementation of this
result set mapping is predicated on the idea that objects can be instantiated and later modified — and this is not
strictly compatible with idiomatic Kotlin. With idiomatic Kotlin, objects are created through their constructors and are
immutable after creation. The examples (especially example03 and example04) will show patterns of dealing with this.

Note that with MyBatis 3.6 and later, we can make the classes fully immutable. Starting with that version, MyBatis is
able to build a nested collection before calling the outer constructor. Example08 shows how to configure this.

### example01 — Auto Mapping

This example shows that MyBatis is able to create simple objects (basic data types, no nested classes) with idiomatic
Kotlin. The "model" classes are Kotlin data classes with a mix of nullable and non-nullable types.  No special mapping
is needed in MyBatis. MyBatis can auto discover class constructors in this case. The important distinction is there are
no nested classes or group-by functions 

### example02 — Type Handlers and Advanced Auto Mapping

This example shows the use of TypeHandlers. With this example we have moved beyond simple types.
We are still using immutable types, but in this case we have a non-standard type that requires a
TypeHandler. Important code changes:
  
1. Look in `/src/main/kotlin/util/YesNoTypeHandler.kt` to see how to write the type handler
2. Look in `/src/test/kotlin/example02/Example02Test.kt` to see how to register the type handler

### example02.oldmybatis - Type Handlers and Advanced Auto Mapping in Older MyBatis Versions

This example shows the use of classes that need TypeHandlers in older versions of MyBatis. In MyBatis versions prior to
3.5.0, MyBatis sometimes had difficulty finding constructors on classes that included types with TypeHandlers.
In that case, we need to annotate the class constructor
with the `@AutomapConstructor` annotation and also register the type handler.  Important code changes:

1. Look in `/src/main/kotlin/example02/oldmybatis/Example02Model.kt` to see how to annotate the class
2. Look in `/src/main/kotlin/util/YesNoTypeHandler.kt` to see how to write the type handler
3. Look in `/src/test/kotlin/example02/oldmybatis/Example02Test.kt` to see how to register the type handler

### example03 — Nested Objects

This example shows how to write Kotlin for a class hierarchy where a class (Person in this case) has an
attribute that is another class (Address in this case).  With this example we can no longer use Kotlin
immutable data classes because of the way that MyBatis constructs an object graph in cases like this. 

Note that this example uses mutable objects, which are not exactly idiomatic Kotlin. This shows how to use MyBatis for
nested collections with MyBatis version prior to 3.6.0. After that version, MyBatis is able to build nested object
graphs that are fully immutable. See Example08 for how to use fully immutable result objects.

### example04 — Join Queries with Nested Collections

This example shows how to use MyBatis support for N+1 query mitigation.  This involves creating a
result map with a nested collection.  Currently, this is only supported in MyBatis using XML to define the
result map.

Note that this example uses mutable objects, which are not exactly idiomatic Kotlin. This shows how to use MyBatis for
nested collections with MyBatis version prior to 3.6.0. After that version, MyBatis is able to build nested object
graphs that are fully immutable. See Example08 for how to use fully immutable result objects.

### example04.lazy - Lazy Loaded Nested Collections

This example shows how to use MyBatis support for lazy loaded associations. This forces an N+1 query (or worse),
so be careful with lazy loading.  There are also issues with lazy loading and Kotlin due to the way that Javassist
works (MyBatis uses Javassist to create dynamic proxies when you use lazy loading). Any class that will be
lazily loaded, and any field in that class that will be lazily loaded, must be declared "open" in Kotlin.
We can code queries like this without XML, which is good, but the performance may be worse — tradeoffs.

### example05 — MyBatis Dynamic SQL

This example shows how to use Kotlin to interact with the "MyBatis Dynamic SQL" library,
and it shows how MyBatis Generator creates code for Kotlin.

The example also shows how to extend the DSL to support a new SQL function. This can be done in two ways:

1. Using Kotlin's experimental Context Parameters feature — which makes for an elegant DSL extension that looks the same
   as the built-in functions
2. Using no experimental features and sacrificing some elegance

### example06 — MyBatis Dynamic SQL (Generated Values)

This example shows how to use Kotlin to interact with the "MyBatis Dynamic SQL" library when a
table contains generated values.
This example also shows how MyBatis Generator creates code for Kotlin.

### example07 — Explicit Constructor Mappings
Example02 showed how MyBatis can automap constructors. Example07 shows how to explicitly map constructors, using both
annotations and XML. The key learning is that Kotlin's handling of primitive types can be confusing. A non-nullable
primitive type maps to a true Java primitive type, but a nullable primitive will map to the Java wrapper type.
In XML configuration, you must use the proper type aliases for these types. The following table shows examples of these
mappings:

| Kotlin Type | Java Type | MyBatis Alias |
|-------------|-----------|---------------|
| Int         | int       | _int          |
| Int?        | Integer   | int           |
| Boolean     | boolean   | _boolean      |
| Boolean?    | Boolean   | boolean       |

### example08 — Fully Immutable Object Graphs
Starting with MyBatis version 3.6, MyBatis can build object graphs with nested objects and collections that are fully
immutable. This example shows how to do this. We still need to define the result maps in XML, but the code is much
more compatible with idiomatic Kotlin.
