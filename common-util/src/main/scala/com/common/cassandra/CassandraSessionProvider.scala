package com.common.cassandra

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.datastax.driver.core._

trait CassandraSessionProvider {

  implicit val actorSystem: ActorSystem
  implicit lazy val materializer = ActorMaterializer()

  private val cassandraHost = "127.0.0.1"

  implicit val cassandraSession: Session = Cluster.builder
    .addContactPoint(cassandraHost)
    .withPort(9042)
    .build
    .connect()

}
