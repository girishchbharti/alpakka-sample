package com.sample.model


case class Student(id: Integer, name: String) {

  def toBindSeq: Seq[AnyRef] = {
    Seq(id, name)
  }

}

