package domain

data class Turn (
  val number: Int,
  val state: State
) {
  enum class State { DrawPhase, StandByPhase, MainPhase1, BattlePhase, MainPhase2, EndPhase }
}