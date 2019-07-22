package com.sample

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.scaladsl.CassandraSink
import akka.stream.scaladsl.Source
import com.common.cassandra.CassandraSessionProvider
import com.common.logger.Logging
import com.datastax.driver.core.Session
import com.sample.model.Student
import com.sample.repositories.StudentRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object GeneratorApp extends App with CassandraSessionProvider with Logging with StudentRepo {

  override implicit val actorSystem: ActorSystem = ActorSystem()
  override implicit val session: Session= cassandraSession

  val students = (10 to 2000).map { elem => Student(elem, "name_" + elem) }.toList
  val sink = CassandraSink[Student](parallelism = 20, preparedStatementInsert, statementBinder)

  val result: Future[Done] = Source(students)
    .map { student =>
      println("Student found.." + student)
      student
    }
    .runWith(sink)

  result.map { result =>
    info(s"Response found: [$result]")
  }.recover {
    case ex: Exception =>
      error(s"Error found while ingesting student into cassandra data: [$ex]")
      throw ex
  }

}
