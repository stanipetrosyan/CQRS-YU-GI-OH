package domain

import java.util.UUID

data class Spell(
  val name: String,
  val type: Type,
  val text: String,
  val effects: List<Effect>
) {
  enum class Type {
    Normal, Continuous, Field, Equip, QuickPlay, Ritual
  }
}

sealed class Effect
data class DrawCards(
  val value: Int
): Effect()
data class DestroyCard(
  val cardId: UUID
): Effect()

data class DiscardCards(
  val value: Int
): Effect()