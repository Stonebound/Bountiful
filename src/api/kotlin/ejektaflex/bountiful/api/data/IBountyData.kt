package ejektaflex.bountiful.api.data

import ejektaflex.bountiful.api.logic.IStageRequirement
import ejektaflex.bountiful.api.logic.picked.IPickedEntry
import ejektaflex.bountiful.api.logic.picked.PickedEntryStack
import ejektaflex.bountiful.api.registry.IValueRegistry
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.util.INBTSerializable

interface IBountyData : INBTSerializable<NBTTagCompound>, IStageRequirement {

    /**
     * How long a bounty has left on the Bounty Board
     */
    var boardStamp: Int

    /**
     * How long the bounty has before it expires.
     */
    var bountyTime: Long

    /**
     * An integer representing the EnumBountyRarity of the bounty
     */
    var rarity: Int

    /**
     * A list of pairs of itemstacks needed to fulfill a bounty, and the unitWorth needed.
     */
    val toGet: IValueRegistry<IPickedEntry>

    /**
     * A list of pairs of itemstacks used as rewardPools for the bounty, and the unitWorth needed.
     */
    val rewards: IValueRegistry<PickedEntryStack>

    /**
     * A long representing the last world time when the bounty was given.
     */
    var bountyStamp: Long?

    /**
     * A long representing how long the bounty has left before it expires.
     */
    fun timeLeft(world: World): Long

    /**
     * Whether or not the bounty has expired.
     */
    fun hasExpired(world: World): Boolean

    /**
     * A long representing how long the bounty has left on the board.
     */
    fun boardTimeLeft(world: World): Long

}
