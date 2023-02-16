package domain

data class Spell(
  val name: String,
  val type: Type,
  val text: String
) {
  enum class Type {
    Normal, Continuous, Field, Equip, QuickPlay, Ritual
  }
}
