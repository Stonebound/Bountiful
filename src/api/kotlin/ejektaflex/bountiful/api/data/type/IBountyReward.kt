package ejektaflex.bountiful.api.data.type

import ejektaflex.bountiful.api.data.IBountyEntry
import net.minecraft.entity.player.EntityPlayer

interface IBountyReward : IBountyEntry {
    /**
     * @return The amount of worth that the reward ended up being worth
     */
    fun handleReward(player: EntityPlayer, data: IBountyEntry): Int
}