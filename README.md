# MyBatis Kotlin Examples

This project demonstrates use of MyBatis with the Kotlin language.  This is a work in progress.

Initially, we are concentrated on how to use MyBatis with Kotlin without substantial changes to any existing library.

Subsequently, we will investigate modifications to MyBatis, or potential Kotlin extensions, that may lead to a more
idiomatic Kotlin integration.

Note that in most cases we are foregoing XML and working solely with the Java API for MyBatis.  Hopefully it will be
clear how to translate the concepts to XML if so desired.

All the code in this project is pure Kotlin. As is typical, source code is in the /src/main/kotlin
directory and test code is in /src/test/kotlin.  The examples described below have associated packages
in the source trees.  So code for example01 is in /src/main/kotlin/example01, etc.  

## Using MyBatis As Is

One of MyBatis' core strengths is mapping SQL result sets to an object graph.  MyBatis is good at these mappings for
many different types of class hierarchies. MyBatis can create a deeply nested object graph and its group by function
provides support for creating an object graph that avoids the need for N+1 queries. However, the implementation of this
result set mapping is predicated on the idea that objects can be instantiated and later modified - and this is not
strictly compatible with idiomatic Kotlin. With idiomatic Kotlin, objects are created through their constructors and are
immutable after creation.

### example01 - Auto Mapping

This example shows that MyBatis is able to create simple objects (basic data types, no nested classes) with idiomatic
Kotlin. The "model" classes are Kotlin data classes with a mix of nullable and non-nullable types.  No special mapping
is need in MyBatis. MyBatis can auto discover class constructors in this case. The important distinction is:

1. Only simple data types are used
2. There are no nested classes or group by functions 

### example02 - Type Handlers

This example shows the use of TypeHandlers. With this example we have moved beyond simple types.
We are still using immutable types, but in this case we have a non standard type that requires a
TypeHandler. In addition to creating the TypeHandler itself, we are now forced to use the `@ConstructorArgs` annotation
on the mapper to specify two things: the data types for the constructor, and the type handler.

### example03 - Nested Objects

This example shows how to write Kotlin code for a class hierarchy where a class (Person in this case) has an
attribute that is another class (Address in this case).  With this example we can no longer use Kotlin
data classes because of the way that MyBatis constructs an object graph in cases like this. 

### example04 - Join Queries with Nested Collections

This example shows how to use MyBatis support for N+1 query mitigation.  This involves creating a
result map with a nested collection.  Currently, this is only supported in MyBatis XML. 

### example05 - MyBatis Dynamic SQL

This example shows how to use Kotlin to interact with the "MyBatis Dynamic SQL" library.
This example also shows how a future version of MyBatis Generator might generate code for Kotlin.
