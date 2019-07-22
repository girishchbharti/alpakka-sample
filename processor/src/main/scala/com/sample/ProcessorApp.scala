package com.sample

import java.util.concurrent.TimeUnit

import akka.Done
import akka.actor.ActorSystem
import akka.routing.{Pool, RoundRobinPool}
import akka.stream.alpakka.cassandra.scaladsl.CassandraSource
import akka.stream.scaladsl.Sink
import akka.util.Timeout
import com.common.cassandra.CassandraSessionProvider
import com.common.logger.Logging
import com.datastax.driver.core.Session
import com.sample.model.Student
import com.sample.repositories.StudentRepo

import scala.concurrent.ExecutionContext.Implicits.global

object ProcessorApp extends App with StudentRepo with CassandraSessionProvider with Logging {

  override implicit val actorSystem: ActorSystem = ActorSystem()
  override implicit val session: Session= cassandraSession
  implicit val timeout: Timeout = Timeout(50, TimeUnit.MILLISECONDS)

  val actorRouter = actorSystem.actorOf(
    RoundRobinPool(10, supervisorStrategy = Pool.defaultSupervisorStrategy)
      .props(ProcessorActor.props(actorSystem))
  )

  val result = CassandraSource(prepareStatementSelect)
    .map { row =>
      info(s"Got a new row from cassandra table : [$row]")
      Student(
        row.getInt("id"),
        row.getString("name")
      )
    }
    .ask[Done](10)(actorRouter)
    .runWith(Sink.ignore)

  result.recover {
    case ex: Exception =>
      error(s"Error found while fetching data from cassandra table: [$ex]")
      throw ex
  }

}
