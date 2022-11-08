package domain

import java.time.LocalDateTime
import java.util.*

enum class MatchState {
  STARTED, CLOSED
}

class Match(private val id: UUID) {
  val changes = mutableListOf<MatchEvent>()
  private var players: List<Player> = emptyList()
  private var monsters: MutableList<Monster> = mutableListOf()
  private var normalSummonPermitted: Int = 1
  private var monstersAttacked: MutableList<UUID> = mutableListOf()
  private var monsterToDestroy: UUID? = null
  private var state: MatchState = MatchState.STARTED
  
  private var turn: Turn = Turn(1, Turn.State.DrawPhase)
  
  fun isPermittedNormalSummon(): Boolean {
    return normalSummonPermitted > 0
  }
  
  fun isPhase(state: Turn.State): Boolean {
    return turn.state == state
  }
  
  fun monsterToDestroy(): UUID? {
    return this.monsterToDestroy
  }
  
  fun changePhaseIsPermitted(phase: Turn.State): Boolean {
    return phase > this.turn.state || (this.turn.state == Turn.State.EndPhase && phase == Turn.State.DrawPhase)
  }
  
  fun monsterCanAttack(id: UUID): Boolean {
    return monstersAttacked.none { it == id }
  }
  
  fun calcDamage(attacker: UUID, defender: UUID? = null): Int {
    return monsters.find { attacker == it.id }!!.attack - (monsters.singleOrNull { defender == it.id }?.attack ?: 0)
  }
  
  fun findOpponent(by: String): String {
    return players.find { it.username != by }!!.username
  }
  
  fun playerLost(username: String, damage: Int): Boolean {
    return (players.find { it.username == username }!!.lifePoints - damage <= 0)
  }
  
  fun start(player: Player, opponent: Player): Match {
    changes.add(MatchStarted(this.id, player, opponent, LocalDateTime.now()))
    return this
  }
  
  fun summonMonster(username: String, monster: Monster): Match {
    changes.add(MonsterNormalSummoned(this.id, username, monster, LocalDateTime.now()))
    return this
  }
  
  fun setMonster(username: String, monster: Monster): Match {
    changes.add(MonsterSet(this.id, username, monster, LocalDateTime.now()))
    return this
  }
  
  
  fun drawCard(username: String): Match {
    changes.add(CardDrew(this.id, username, LocalDateTime.now()))
    return this
  }
  
  
  fun changeTurnPhase(username: String, phase: Turn.State): Match {
    changes.add(
      when(phase) {
        Turn.State.DrawPhase -> DrawPhaseSet(this.id, username, LocalDateTime.now())
        Turn.State.StandByPhase -> StandbyPhaseSet(this.id, username, LocalDateTime.now())
        Turn.State.MainPhase1 -> MainPhaseOneSet(this.id, username, LocalDateTime.now())
        Turn.State.BattlePhase -> BattlePhaseSet(this.id, username, LocalDateTime.now())
        Turn.State.MainPhase2 -> MainPhaseTwoSet(this.id, username, LocalDateTime.now())
        Turn.State.EndPhase -> EndPhaseSet(this.id, username, LocalDateTime.now())
      }
    )
    return this
  }
  
  fun declareDirectAttack(username: String, monsterId: UUID): Match {
    changes.add(DirectAttackDeclared(this.id, monsterId, username, LocalDateTime.now()))
    return this
  }
  
  fun declareAttack(username: String, attacker: UUID, defender: UUID): Match {
    changes.add(AttackDeclared(this.id, attacker, defender, username, LocalDateTime.now()))
    return this
  }
  
  fun inflictDamage(username: String, damage: Int, type: DamageType): Match {
    val event = when(type) {
      DamageType.Battle -> BattleDamageInflicted(this.id, username, damage, LocalDateTime.now())
      DamageType.Direct -> DirectDamageInflicted(this.id, username, damage, LocalDateTime.now())
      DamageType.Effect -> EffectDamageInflicted(this.id, username, damage, LocalDateTime.now())
    }
    changes.add(event)
    return this
  }
  
  fun destroyMonster(username: String, monsterId: UUID): Match {
    changes.add(MonsterDestroyed(this.id, username, monsterId, LocalDateTime.now()))
    return this
  }
  
  fun end(username: String): Match {
    changes.add(MatchEnded(this.id, username, LocalDateTime.now()))
    return this
  }
  
  
  fun hydrate(events: List<MatchEvent>): Match {
    events.forEach { event ->
      when (event) {
        is MatchStarted -> apply(event)
        is CardDrew -> apply(event)
        is MonsterNormalSummoned -> apply(event)
        is MonsterSet -> TODO()
        is DrawPhaseSet -> apply(event)
        is StandbyPhaseSet -> apply(event)
        is MainPhaseOneSet -> apply(event)
        is BattlePhaseSet -> apply(event)
        is MainPhaseTwoSet -> apply(event)
        is EndPhaseSet -> apply(event)
        is DirectAttackDeclared -> apply(event)
        is AttackDeclared -> apply(event)
        is BattleDamageInflicted -> apply(event)
        is DirectDamageInflicted -> apply(event)
        is EffectDamageInflicted -> apply(event)
        is MonsterDestroyed -> apply(event)
        is MatchEnded -> apply(event)
      }
    }
    
    return this
  }
  
  private fun apply(event: MatchEnded) {
    this.state = MatchState.CLOSED
  }
  
  private fun apply(event: MonsterDestroyed) {
    this.monsters.removeIf { it.id == event.monsterId }
  }
  
  private fun apply(event: DirectAttackDeclared) {
    this.monstersAttacked.add(event.attackerId)
  }
  
  private fun apply(event: AttackDeclared) {
    this.monstersAttacked.add(event.attackerId)
    this.monsterToDestroy = event.defenderId
  }
  
  private fun apply(event: BattleDamageInflicted) {
    inflictDamage(event)
  }
  
  private fun apply(event: DirectDamageInflicted) {
    inflictDamage(event)
  }
  
  private fun apply(event: EffectDamageInflicted) {
    inflictDamage(event)
  }
  
  
  private fun apply(event: DrawPhaseSet) {
    this.turn = this.turn.copy(state = Turn.State.DrawPhase)
  }
  
  private fun apply(event: StandbyPhaseSet) {
    this.turn = this.turn.copy(state = Turn.State.StandByPhase)
  }
  
  private fun apply(event: MainPhaseOneSet) {
    this.turn = this.turn.copy(state = Turn.State.MainPhase1)
  }
  
  private fun apply(event: BattlePhaseSet) {
    this.turn = this.turn.copy(state = Turn.State.BattlePhase)
  }
  
  private fun apply(event: MainPhaseTwoSet) {
    this.turn = this.turn.copy(state = Turn.State.MainPhase2)
  }
  
  private fun apply(event: EndPhaseSet) {
    this.turn = this.turn.copy(state = Turn.State.EndPhase)
    this.normalSummonPermitted = 1
    this.monstersAttacked.clear()
  }
  
  private fun apply(event: MatchStarted) {
    this.players = this.players + event.player + event.opponent
  }
  
  private fun apply(event: CardDrew) {
    val player = this.players.single { it.username == event.by}
    this.players = this.players.filter { it.username != event.by} + player.copy(deckSize = player.deckSize - 1)
  }
  
  private fun apply(event: MonsterNormalSummoned) {
    this.monsters.add(event.monster)
    this.normalSummonPermitted -= 1
  }
  
  private fun inflictDamage(event: DamageEvent) {
    val player = this.players.single { it.username == event.by }
    val filtered = this.players.filter { it.username != event.by }
    this.players = filtered + player.copy(lifePoints = player.lifePoints - event.damage)
  }
}

