package ejektaflex.bountiful.registry

import ejektaflex.bountiful.Bountiful
import ejektaflex.bountiful.api.logic.picked.IPickedEntry
import ejektaflex.compat.FacadeGameStages
import net.minecraft.world.World

// Currently unused? Should only need a PoolRegistry and a DecreeRegistry
open class EntryRegistry : ValueRegistry<IPickedEntry>() {

    fun validEntries(world: World, worth: Int, alreadyPicked: List<String>): List<IPickedEntry> {
        return validEntries(world).filter { it.minValueOfPick <= worth && it.content !in alreadyPicked }.sortedBy { it.minValueOfPick }
    }

    fun validEntries(world: World): List<IPickedEntry> {
        // Was told client does not always know about all players
        return if (world.isRemote) {
            listOf()
        } else {
            return if (Bountiful.config.isRunningGameStages) {
                content.filter { FacadeGameStages.anyPlayerHas(world, it.requiredStages()) }
            } else {
                content
            }
        }
    }
}