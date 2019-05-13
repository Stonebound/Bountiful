package ejektaflex.bountiful.api.data.type

import ejektaflex.bountiful.api.data.IBountyEntry
import net.minecraft.entity.player.EntityPlayer

interface IBountyObjective : IBountyEntry {
    /**
     * @return Whether or not the objective was considered to be complete after handling
     */
    fun handleObjective(player: EntityPlayer, data: IBountyEntry): Boolean
}