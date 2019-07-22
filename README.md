Alpakka project is an open-source initiative to implement stream aware and reactive pipelines using Java and Scala which is built on top of Akka streams and specially designed to provide a DSL for reactive and stream-oriented programming with built-in support for back-pressure to avoid the flood of data. This sample project provide an implementation to read and write data from/to Cassandra using Alpakka.

CREATE CASSANDRA TABLES:
--------------

cqlsh> create table alpakka_sample.student(id int PRIMARY KEY, name text);



RUN DATA GENERATOR NODE:
-----------------------

sbt "project generator" run


RUN DATA PROCESSOR NODE:
------------------------

sbt "project processor" run
