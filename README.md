movie-name-matcher
==================

Apache Lucene based movie name matcher. Matches ambiguous movie names to the available movie names. 

Assuming that we have millions of movie names and each one is associated with unique identifier.

The problem is writing an API that will take an ambiguous movie name as input, match it with the closest movie name in the database/index and return the identifier.

In this case, the ambiguous movie name is the query and the correct movie name is the document.

For example we may have "The Lord of Rings" in our index, we would like to return its identifier for movie names like "lord of rings", "lord of rings, the", "lord rings" etc.

Since the order of the words is preserved in this use case, ShingleFilter provided by Lucene was useful in achieving the desired results.

Shingling breaks the query/document into group of words in order thereby preserving the order.
Eg: "Lord of Rings" would be broken into "Lord of" and "of Rings" tokens.

Add following jars to classpath:
lucene-analyzers-common-4.1.0.jar
lucene-core-4.1.0.jar
lucene-queryparser-4.1.0.jar
