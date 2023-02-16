package domain

import java.time.LocalDateTime
import java.util.*

interface Event {
  val matchId: UUID
  val at: LocalDateTime
}

sealed interface MatchEvent : Event {}

sealed interface PlayerEvent: MatchEvent {
  val by: String
}

sealed interface DamageEvent : PlayerEvent {
  val damage: Int
}

data class MatchStarted(
  override val matchId: UUID,
  val player: Player,
  val opponent: Player,
  override val at: LocalDateTime
): MatchEvent

data class MonsterNormalSummoned(
  override val matchId: UUID,
  override val by: String,
  val monster: Monster,
  override val at: LocalDateTime
): PlayerEvent

data class MonsterSet(
  override val matchId: UUID,
  override val by: String,
  val monster: Monster,
  override val at: LocalDateTime
): PlayerEvent

data class CardDrew(
  override val matchId: UUID,
  override val by: String,
  override val at: LocalDateTime
): PlayerEvent

data class DrawPhaseSet(
  override val matchId: UUID,
  override val by: String,
  override val at: LocalDateTime
): PlayerEvent

data class StandbyPhaseSet(
  override val matchId: UUID,
  override val by: String,
  override val at: LocalDateTime
): PlayerEvent
data class MainPhaseOneSet(
  override val matchId: UUID,
  override val by: String,
  override val at: LocalDateTime
): PlayerEvent
data class BattlePhaseSet(
  override val matchId: UUID,
  override val by: String,
  override val at: LocalDateTime
): PlayerEvent
data class MainPhaseTwoSet(
  override val matchId: UUID,
  override val by: String,
  override val at: LocalDateTime
): PlayerEvent
data class EndPhaseSet(
  override val matchId: UUID,
  override val by: String,
  override val at: LocalDateTime
): PlayerEvent

data class AttackDeclared(
  override val matchId: UUID,
  val attackerId: UUID,
  val defenderId: UUID,
  override val by: String,
  override val at: LocalDateTime
): PlayerEvent

data class BattleDamageInflicted(
  override val matchId: UUID,
  override val by: String,
  override val damage: Int,
  override val at: LocalDateTime
): DamageEvent

data class DirectDamageInflicted(
  override val matchId: UUID,
  override val by: String,
  override val damage: Int,
  override val at: LocalDateTime
): DamageEvent

data class EffectDamageInflicted(
  override val matchId: UUID,
  override val by: String,
  override val damage: Int,
  override val at: LocalDateTime
): DamageEvent

data class DirectAttackDeclared(
  override val matchId: UUID,
  val attackerId: UUID,
  override val by: String,
  override val at: LocalDateTime
) : PlayerEvent

data class MonsterDestroyed(
  override val matchId: UUID,
  override val by: String,
  val monsterId: UUID,
  override val at: LocalDateTime
) : PlayerEvent

data class SpellActivated(
  override val matchId: UUID,
  override val by: String,
  val spell: Spell,
  override val at: LocalDateTime
): PlayerEvent

data class MatchEnded(
  override val matchId: UUID,
  val winner: String,
  override val at: LocalDateTime
): MatchEvent




