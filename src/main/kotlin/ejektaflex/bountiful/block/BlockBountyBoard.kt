package ejektaflex.bountiful.block

import ejektaflex.bountiful.Bountiful
import ejektaflex.bountiful.api.BountifulAPI
import ejektaflex.bountiful.api.ext.filledSlots
import ejektaflex.bountiful.gui.GuiHandler
import ejektaflex.bountiful.item.ItemBounty
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.*
import java.util.*

class BlockBountyBoard(props: Block.Properties) : BlockTileEntity<TileEntityBountyBoard>(props, "bountyboard") {

    override fun getTranslationKey(): String {
        return "bountiful.bountyboardblock"
    }


    override val tileEntityClass: Class<TileEntityBountyBoard>
        get() = TileEntityBountyBoard::class.java

    override fun onBlockActivated(state: IBlockState, world: World?, pos: BlockPos?, player: EntityPlayer?, hand: EnumHand, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!world!!.isRemote && pos != null) {
            val holding = player?.getHeldItem(hand!!)
            val tile = (world.getTileEntity(pos)!! as TileEntityBountyBoard)
            if (!player!!.isSneaking) {
                // Cash in, or else try get bounty
                if (Bountiful.config.cashInAtBountyBoard && holding?.item is ItemBounty) {
                    (holding.item as ItemBounty).cashIn(player, hand!!, atBoard = true)
                } else if (tile.inventory.handler.filledSlots.isEmpty()) {
                    //player.sendTranslation("bountiful.board.empty")
                } else {
                    // TODO Figure out how a player can open a gui
                    //player.openGui(Bountiful.instance!!, GuiHandler.BOARD_GUI, world, pos.x, pos.y, pos.z)
                }
            }
        }
        return true
    }

    override fun getWeakPower(blockState: IBlockState, blockAccess: IBlockReader, pos: BlockPos, side: EnumFacing): Int {
        return if (blockAccess is World) {
            val tile = (getTileEntity(blockAccess, pos) as TileEntityBountyBoard)
            if (tile.pulseLeft > 0) 15 else 0
        } else {
            0
        }
    }

    override fun shouldCheckWeakPower(state: IBlockState?, world: IWorldReader?, pos: BlockPos?, side: EnumFacing?): Boolean {
        return false
    }

    override fun canProvidePower(state: IBlockState): Boolean {
        return true
    }

    override fun hasComparatorInputOverride(state: IBlockState): Boolean {
        return true
    }

    override fun quantityDropped(state: IBlockState, random: Random): Int {
        return if (BountifulAPI.config.boardDrops) super.quantityDropped(state, random) else 0
    }

    // TODO Figure out how bonuses apply to quantity of block items dropped
    /*
    override fun quantityDroppedWithBonus(fortune: Int, random: Random): Int {
        return if (BountifulAPI.config.boardDrops) super.quantityDroppedWithBonus(fortune, random) else 0
    }
    */

    override fun getComparatorInputOverride(blockState: IBlockState, worldIn: World, pos: BlockPos): Int {
        val tile = (getTileEntity(worldIn, pos) as TileEntityBountyBoard)
        return (tile.inventory.handler.filledSlots.size / 2)
    }

    // Initial population of board when placed
    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase?, stack: ItemStack) {
        val tile = (getTileEntity(worldIn, pos) as TileEntityBountyBoard)
        for (i in 0 until Bountiful.config.bountiesCreatedOnPlace) {
            tile.inventory.addSingleBounty(worldIn, tile)
            tile.markDirty()
        }
    }

    override fun createTileEntity(state: IBlockState?, world: IBlockReader?): TileEntityBountyBoard {
        return TileEntityBountyBoard()
    }

}
