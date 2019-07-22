

CREATE CASSANDRA TABLES:
--------------

cqlsh> create table alpakka_sample.student(id int PRIMARY KEY, name text);



RUN DATA GENERATOR NODE:
-----------------------

sbt "project generator" run


RUN DATA PROCESSOR NODE:
------------------------

sbt "project processor" run
