package ejektaflex.bountiful

import ejektaflex.bountiful.api.IBountifulAPI
import ejektaflex.bountiful.api.block.ITileEntityBountyBoard
import ejektaflex.bountiful.api.config.IBountifulConfig
import ejektaflex.bountiful.api.ext.stacks
import ejektaflex.bountiful.api.data.IBountyEntry
import ejektaflex.bountiful.api.enum.EnumBountyRarity
import ejektaflex.bountiful.block.TileEntityBountyBoard
import ejektaflex.bountiful.data.BountyEntry
import ejektaflex.bountiful.logic.BountyCreator
import ejektaflex.bountiful.registry.BountyRegistry
import ejektaflex.bountiful.registry.RewardRegistry
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object InternalAPI : IBountifulAPI {

    override val bountyRegistry = BountyRegistry
    override val rewardRegistry = RewardRegistry

    override fun getBountiesAt(worldIn: World, pos: BlockPos): List<ItemStack>? {
        return getBountyBoard(worldIn, pos)?.inventory?.handler?.stacks
    }

    override fun getBountyBoard(worldIn: World, pos: BlockPos): ITileEntityBountyBoard? {
        return worldIn.getTileEntity(pos) as? TileEntityBountyBoard
    }

    override fun toBountyData(stack: ItemStack): IBountyEntry {
        return BountyEntry.from(stack)
    }

    override fun dataToStack(entry: IBountyEntry): ItemStack {
        return ItemStack(ContentRegistry.bounty).apply { this.tagCompound = entry.serializeNBT() }
    }

    override val config: IBountifulConfig
        get() = Bountiful.config

    override fun createBountyData(worldIn: World, rarity: EnumBountyRarity?): BountyEntry? {
        return BountyCreator.create(worldIn, rarity)
    }

}