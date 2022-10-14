package domain

import java.util.*


interface Command

sealed interface MatchCommand: Command {
  val matchId: UUID
  val by: String
}

data class StartMatch(
  override val matchId: UUID,
  override val by: String,
  val player: Player,
  val opponent: Player
): MatchCommand

data class SetDrawPhase(
  override val matchId: UUID,
  override val by: String,
): MatchCommand

data class SetStandByPhase(
  override val matchId: UUID,
  override val by: String,
): MatchCommand

data class SetMainPhaseOne(
  override val matchId: UUID,
  override val by: String,
): MatchCommand

data class SetBattlePhase(
  override val matchId: UUID,
  override val by: String,
): MatchCommand

data class SetMainPhaseTwo(
  override val matchId: UUID,
  override val by: String,
): MatchCommand

data class SetEndPhase(
  override val matchId: UUID,
  override val by: String,
): MatchCommand

data class DrawCard(
  override val matchId: UUID,
  override val by: String
): MatchCommand

data class NormalSummonMonster(
  override val matchId: UUID,
  override val by: String,
  val monster: Monster,
) : MatchCommand

data class SetSummonMonster(
  override val matchId: UUID,
  override val by: String,
  val monster: Monster,
) : MatchCommand

data class DeclareDirectAttack(
  override val matchId: UUID,
  override val by: String,
  val to: String,
  val monsterId: UUID
) : MatchCommand

data class InflictDamage(
  override val matchId: UUID,
  override val by: String,
  val damage: Int,
  val type: DamageType
) : MatchCommand

data class DeclareAttack(
  override val matchId: UUID,
  override val by: String,
  val to: String,
  val attacker: UUID,
  val defender: UUID
) : MatchCommand

data class DestroyMonster(
  override val matchId: UUID,
  override val by: String,
  val monsterId: UUID
) : MatchCommand

enum class DamageType {
  Battle, Direct, Effect
}