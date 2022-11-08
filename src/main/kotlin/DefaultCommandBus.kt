import domain.*
import handler.command.*
import port.CommandBus
import port.CommandResult

class DefaultCommandBus(private val matches: Matches): CommandBus {
  override fun send(command: MatchCommand): CommandResult {
    return when(command) {
      is StartMatch -> StartMatchCommandHandler(matches).handle(command)
      is DrawCard -> DrawCardCommandHandler(matches).handle(command)
      is NormalSummonMonster -> NormalSummonMonsterCommandHandler(matches).handle(command)
      is SetMonster -> SetMonsterCommandHandler(matches).handle(command)
      is DeclareDirectAttack -> DeclareDirectAttackCommandHandler(matches).handle(command)
      is SetDrawPhase -> SetDrawPhaseCommandHandler(matches).handle(command)
      is SetStandByPhase -> SetStandByPhaseCommandHandler(matches).handle(command)
      is SetMainPhaseOne -> SetMainPhaseOneCommandHandler(matches).handle(command)
      is SetBattlePhase -> SetBattlePhaseCommandHandler(matches).handle(command)
      is SetMainPhaseTwo -> SetMainPhaseTwoCommandHandler(matches).handle(command)
      is SetEndPhase -> SetEndPhaseCommandHandler(matches).handle(command)
      is DeclareAttack -> DeclareAttackCommandHandler(matches).handle(command)
      is InflictDamage -> InflictDamageCommandHandler(matches).handle(command)
      is DestroyMonster -> DestroyMonsterCommandHandler(matches).handle(command)
      is EndMatch -> EndMatchCommandHandler(matches).handle(command)
    }
  }
  
}