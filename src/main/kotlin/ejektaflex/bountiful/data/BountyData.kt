package ejektaflex.bountiful.data

import ejektaflex.bountiful.Bountiful
import ejektaflex.bountiful.api.ext.getPickedEntryList
import ejektaflex.bountiful.api.ext.getUnsortedList
import ejektaflex.bountiful.api.ext.setUnsortedList
import ejektaflex.bountiful.api.data.IBountyData
import ejektaflex.bountiful.api.ext.modOriginName
import ejektaflex.bountiful.api.item.IItemBounty
import ejektaflex.bountiful.api.logic.BountyNBT
import ejektaflex.bountiful.api.logic.picked.IPickedEntry
import ejektaflex.bountiful.api.logic.picked.PickedEntry
import ejektaflex.bountiful.api.logic.picked.PickedEntryStack
import ejektaflex.bountiful.item.ItemBounty
import ejektaflex.bountiful.registry.ValueRegistry
import ejektaflex.compat.FacadeGameStages
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import kotlin.math.max

class BountyData : IBountyData {

    // 72000 = 1 hour IRL
    // TODO Reimplement config option
    //override var boardStamp = Bountiful.config.boardLifespan
    override var boardStamp = 6000

    override var bountyTime = 0L
    override var rarity = 0
    override val toGet = ValueRegistry<IPickedEntry>()
    override val rewards = ValueRegistry<PickedEntryStack>()
    override var bountyStamp: Long? = null
    var worth = 0

    override fun timeLeft(world: World): Long {
        return if (bountyStamp == null) {
            bountyTime
        } else {
            max(bountyStamp!! + bountyTime - world.gameTime, 0)
        }
    }

    override fun hasExpired(world: World): Boolean {
        return timeLeft(world) <= 0
    }

    override fun boardTimeLeft(world: World): Long {
        return max(boardStamp + Bountiful.config.boardLifespan - world.gameTime , 0).toLong()
    }

    fun tooltipInfo(world: World, advanced: Boolean): List<String> {
        if (Bountiful.config.isRunningGameStages) {
            val localPlayer = Minecraft.getInstance().player
            if (FacadeGameStages.stagesStillNeededFor(localPlayer, this).isNotEmpty()) {
                return listOf(I18n.format("bountiful.tooltip.requirements"))
            }
        }

        return when (advanced) {
            false -> tooltipInfoBasic(world)
            true -> tooltipInfoAdvanced(world)
        }
    }

    private fun tooltipInfoBasic(world: World): List<String> {
        return listOf(
                //"Board Time: ${formatTickTime(boardTimeLeft(world) / boardTickFreq)}",
                "${I18n.format("bountiful.tooltip.time")}: ${formatTimeExpirable(timeLeft(world) / bountyTickFreq)}",
                getPretty,
                rewardPretty,
                I18n.format("bountiful.tooltip.advanced")
        )
    }

    private fun tooltipInfoAdvanced(world: World): List<String> {
        val cycleLength = 27 // ticks per cycle

        val getItems: List<Pair<IPickedEntry, String>> =  toGet.items.filter { it is PickedEntryStack }.map { it to I18n.format("bountiful.tooltip.requiredDetails") }
        val getRewards: List<Pair<IPickedEntry, String>> =  rewards.items.map { it to I18n.format("bountiful.tooltip.rewardsDetails") }
        val allGets = getItems + getRewards

        return if (allGets.isEmpty()) {
            listOf()
        } else {
            val itemIndex = (world.gameTime % (allGets.size * cycleLength)) / cycleLength
            val itemToShow = allGets[itemIndex.toInt()]
            val istack = (itemToShow.first as PickedEntryStack).itemStack!!
            val la = listOf(itemToShow.second)
            val lb = istack.getTooltip(null) { false }
            val lc = istack.modOriginName?.let { listOf("§9§o$it§r") } ?: listOf()
            return la + lb.map { it.unformattedComponentText } + lc
        }
    }

    private fun formatTickTime(n: Long): String {
        return if (n / 60 <= 0) {
            "${n}s"
        } else {
            "${n / 60}m ${n % 60}s"
        }
    }

    private fun formatTimeExpirable(n: Long): String {
        return if (n <= 0) {
            "§4${I18n.format("bountiful.tooltip.expired")}"
        } else {
            formatTickTime(n)
        }
    }

    private val getPretty: String
        get() {
            return if (toGet.items.isEmpty()) {
                "§6Completed. §aTurn it in!"
            } else {
                "§f${I18n.format("bountiful.tooltip.required")}: " + toGet.items.joinToString(", ") {
                    it.prettyContent
                } + "§r"
            }

        }

    private val rewardPretty: String
        get() {
            return "§f${I18n.format("bountiful.tooltip.rewards")}: " + rewards.items.joinToString(", ") {
                "§f${it.amount}x §6${it.itemStack?.displayName}§f"
            } + "§r"
        }

    override fun requiredStages(): List<String> {
        return toGet.items.map { it.requiredStages() }.flatten() + rewards.items.map { it.requiredStages() }.flatten()
    }

    override fun deserializeNBT(tag: NBTTagCompound) {
        boardStamp = tag.getInt(BountyNBT.BoardStamp.key)
        bountyTime = tag.getLong(BountyNBT.BountyTime.key)
        rarity = tag.getInt(BountyNBT.Rarity.key)
        worth = tag.getInt(BountyNBT.Worth.key)
        if (tag.hasKey(BountyNBT.BountyStamp.key)) {
            bountyStamp = tag.getLong(BountyNBT.BountyStamp.key)
        }
        toGet.restore(
                tag.getPickedEntryList(BountyNBT.ToGet.key).toList()
        )

        rewards.restore(tag.getUnsortedList(BountyNBT.Rewards.key) { PickedEntryStack(PickedEntry()) }.toList() )
    }

    override fun serializeNBT(): NBTTagCompound {
        return NBTTagCompound().apply {
            setInt(BountyNBT.BoardStamp.key, boardStamp)
            setLong(BountyNBT.BountyTime.key, bountyTime)
            setInt(BountyNBT.Rarity.key, rarity)
            setInt(BountyNBT.Worth.key, worth)
            bountyStamp?.let { setLong(BountyNBT.BountyStamp.key, it) }
            setUnsortedList(BountyNBT.ToGet.key, toGet.items.toSet())
            setUnsortedList(BountyNBT.Rewards.key, rewards.items.toSet())
        }
    }

    companion object {
        const val bountyTickFreq = 20L
        const val boardTickFreq = 20L

        fun isValidBounty(stack: ItemStack): Boolean {
            return try {
                from(stack)
                true
            } catch (e: Exception) {
                false
            }
        }

        fun from(stack: ItemStack): BountyData {
            if (stack.item is ItemBounty) {
                return (stack.item as IItemBounty).getBountyData(stack) as BountyData
            } else {
                throw Exception("${stack.displayName} is not an IItemBounty and cannot be converted to bounty data!")
            }
        }

    }

}