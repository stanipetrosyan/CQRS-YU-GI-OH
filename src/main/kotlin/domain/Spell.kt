package domain

import java.util.UUID

data class Spell(
  val name: String,
  val type: Type,
  val text: String,
  val effect: Effect
) {
  enum class Type {
    Normal, Continuous, Field, Equip, QuickPlay, Ritual
  }
  
  data class Effect(
    val type: EffectType,
  )
}

sealed interface EffectType
data class DrawCards(
  val value: Int
): EffectType
data class DestroyCard(
  val cardId: UUID
): EffectType