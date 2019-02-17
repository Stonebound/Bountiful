package ejektaflex.bountiful.logic

import ejektaflex.bountiful.api.ext.registryName
import ejektaflex.bountiful.api.logic.picked.PickedEntryEntity
import ejektaflex.bountiful.api.logic.picked.PickedEntryStack
import ejektaflex.bountiful.data.BountyData
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.items.ItemHandlerHelper
import kotlin.math.min

object BountyChecker {

    /**
     * Simply checks whether the two stacks are the same item and have the same NBT data.
     */
    private fun validStackCheck(stack: ItemStack, other: ItemStack): Boolean {
        return stack.isItemEqualIgnoreDurability(other) && ItemStack.areItemStackTagsEqual(stack, other)
    }

    fun hasItems(player: EntityPlayer, inv: NonNullList<ItemStack>, data: BountyData): List<ItemStack>? {
        val stackPicked = data.toGet.items.mapNotNull { it as? PickedEntryStack }

        println(stackPicked)

        val prereqItems = inv.filter { invItem ->
            stackPicked.any { picked ->
                picked.itemStack?.isItemEqualIgnoreDurability(invItem) == true
            }
        }

        println("Prereq items: $prereqItems")

        // Check to see if bounty meets all prerequisites
        val hasAllItems = stackPicked.all { picked ->
            val stacksMatching = prereqItems.filter { validStackCheck(it, picked.itemStack!!) }
            val hasEnough = stacksMatching.sumBy { it.count } >= picked.amount
            if (!hasEnough) {
                //player.sendTranslation("bountiful.cannot.fulfill")
            }
            hasEnough
        }

        return if (hasAllItems) {
            prereqItems
        } else {
            null
        }
    }

    fun takeItems(player: EntityPlayer, inv: NonNullList<ItemStack>, data: BountyData, matched: List<ItemStack>) {
        // If it does, reduce count of all relevant stacks
        data.toGet.items.mapNotNull { it as? PickedEntryStack }.forEach { picked ->
            val stacksToChange = matched.filter { validStackCheck(it, picked.itemStack!!) }
            for (stack in stacksToChange) {
                if (picked.amount == 0) {
                    break
                }
                val amountToRemove = min(stack.count, picked.amount)
                stack.count -= amountToRemove
                picked.amount -= amountToRemove
            }
        }
    }

    /**
     * Tries to tick all relevant entities on Bounty. Returns true if there are none left
     */
    fun tryTakeEntities(player: EntityPlayer, data: BountyData, bounty: ItemStack, entity: EntityLivingBase) {
        // Don't try take entities from an expired bounty.
        if (data.hasExpired(player.world)) {
            return
        }

        val bountyEntities = data.toGet.items.mapNotNull { it as? PickedEntryEntity }

        bountyEntities.forEach { picked ->
            if (picked.entityEntry?.registryName?.toString() == entity.registryName?.toString()) {
                if (picked.killedAmount < picked.amount) {
                    picked.killedAmount++
                }
            }
        }
        bounty.tag = data.serializeNBT()
    }

    fun hasEntitiesFulfilled(data: BountyData): Boolean {
        val bountyEntities = data.toGet.items.mapNotNull { it as? PickedEntryEntity }
        return if (bountyEntities.isEmpty()) {
            true
        } else {
            bountyEntities.all { it.killedAmount == it.amount }
        }
    }

    fun rewardItems(player: EntityPlayer, data: BountyData, bountyItem: ItemStack) {

        // Reward player with rewards
        data.rewards.items.forEach { reward ->
            var amountNeededToGive = reward.amount
            val stacksToGive = mutableListOf<ItemStack>()
            while (amountNeededToGive > 0) {
                val stackSize = min(amountNeededToGive, bountyItem.maxStackSize)
                val newStack = reward.itemStack!!.copy().apply { count = stackSize }

                stacksToGive.add(newStack)
                amountNeededToGive -= stackSize
            }
            stacksToGive.forEach { stack ->
                ItemHandlerHelper.giveItemToPlayer(player, stack)
            }
        }
    }


}