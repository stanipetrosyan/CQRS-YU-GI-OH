package domain

import java.time.LocalDateTime
import java.util.*

interface Event {
  val matchId: UUID
  val at: LocalDateTime
}
sealed interface MatchEvent : Event

data class MatchStarted(
  override val matchId: UUID,
  val player: Player,
  val opponent: Player,
  override val at: LocalDateTime
): MatchEvent

data class MonsterNormalSummoned(
  override val matchId: UUID,
  val monster: Monster,
  override val at: LocalDateTime
): MatchEvent

data class CardDrew(
  override val matchId: UUID,
  val by: String,
  override val at: LocalDateTime
): MatchEvent

data class DrawPhaseSet(
  override val matchId: UUID,
  val by: String,
  override val at: LocalDateTime
): MatchEvent
data class StandbyPhaseSet(
  override val matchId: UUID,
  val by: String,
  override val at: LocalDateTime
): MatchEvent
data class MainPhaseOneSet(
  override val matchId: UUID,
  val by: String,
  override val at: LocalDateTime
): MatchEvent
data class BattlePhaseSet(
  override val matchId: UUID,
  val by: String,
  override val at: LocalDateTime
): MatchEvent
data class MainPhaseTwoSet(
  override val matchId: UUID,
  val by: String,
  override val at: LocalDateTime
): MatchEvent
data class EndPhaseSet(
  override val matchId: UUID,
  val by: String,
  override val at: LocalDateTime
): MatchEvent

data class AttackDeclared(
  override val matchId: UUID,
  val attackerId: UUID,
  val defenderId: UUID,
  val by: String,
  override val at: LocalDateTime
): MatchEvent

data class DamageInflicted(
  override val matchId: UUID,
  val by: String,
  val damage: Int,
  override val at: LocalDateTime
): MatchEvent

data class DirectAttackDeclared(
  override val matchId: UUID,
  val attackerId: UUID,
  val by: String,
  override val at: LocalDateTime
) : MatchEvent





