package domain

import java.util.*

data class Monster(
  val id: UUID,
  val name: String,
  val level: Int,
  val attack: Int,
  val defense: Int,
  val type: MonsterType,
  val description: String
)

enum class MonsterType {
  Normal, Effect
}

enum class SummonType {
  Normal, Special
}