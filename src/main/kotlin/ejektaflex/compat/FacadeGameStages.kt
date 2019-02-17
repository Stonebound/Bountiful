package ejektaflex.compat

import ejektaflex.bountiful.data.BountyData
//import net.darkhax.gamestages.GameStageHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

// TODO Reimplement GameStages support when GameStages releases
object FacadeGameStages {
    fun anyPlayerHas(world: World, stages: List<String>): Boolean {
        /**
        val allPlayers = world.minecraftServer?.playerList?.players
        return allPlayers?.any { GameStageHelper.hasAllOf(it, stages) } ?: false
        */
        return true
    }

    fun stagesStillNeededFor(player: EntityPlayer, bountyData: BountyData): List<String> {
        return stagesStillNeededFor(player, bountyData.requiredStages())
    }

    fun stagesStillNeededFor(player: EntityPlayer, current: List<String>): List<String> {
        //return (current.toSet() - GameStageHelper.getPlayerData(player).stages.filterNotNull().toSet()).toList()
        return listOf()
    }
}