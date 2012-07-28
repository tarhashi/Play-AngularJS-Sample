package models

import org.squeryl.Schema
import org.squeryl.KeyedEntity

case class Task(id: Long, label: String, done: Boolean) extends KeyedEntity[Long]

object AppDB extends Schema {
  val taskTable = table[Task]("task")
}