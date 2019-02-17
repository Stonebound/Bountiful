package ejektaflex.bountiful.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.world.World
import net.minecraft.util.math.BlockPos
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader


abstract class BlockTileEntity<TE : TileEntity>(props: Block.Properties, name: String) : Block(props) {

    abstract val tileEntityClass: Class<TE>

    @Suppress("UNCHECKED_CAST")
    fun getTileEntity(world: World, pos: BlockPos): TE? {
        return world.getTileEntity(pos) as? TE
    }

    override fun hasTileEntity(state: IBlockState): Boolean {
        return true
    }


    abstract override fun createTileEntity(state: IBlockState?, world: IBlockReader?): TE

}