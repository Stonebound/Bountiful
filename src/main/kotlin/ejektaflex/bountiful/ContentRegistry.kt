package ejektaflex.bountiful

import ejektaflex.bountiful.block.BlockBountyBoard
import ejektaflex.bountiful.block.BlockTileEntity
import ejektaflex.bountiful.item.ItemBounty
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.ForgeRegistries


object ContentRegistry {

    val bounty = ItemBounty(
            Item.Properties().maxStackSize(1)
    ).apply {
        registryName = ResourceLocation(BountifulInfo.MODID, "bounty")
    }

    val bountyBlock = BlockBountyBoard(
            Block.Properties.create(Material.WOOD).hardnessAndResistance(
                    if (Bountiful.config.bountyBoardBreakable) {
                        2f
                    } else {
                        -1f
                    }
            )
    ).apply {
            registryName = ResourceLocation(BountifulInfo.MODID, "bountyboard")
    }

    val bountyItemBlock = ItemBlock(bountyBlock, Item.Properties()).apply {
        registryName = ResourceLocation(BountifulInfo.MODID, "bountyboarditem")
        // TODO Implement translation key for item block of Bounty Board
        //translationKey = "bountiful.bountyboarditem"
    }

    val blocks = listOf(
            bountyBlock
    )

    val items = listOf(
            bounty,
            bountyItemBlock
    )

    @SubscribeEvent
    fun registerBlocks(event: RegistryEvent.Register<Block>) {



        event.registry.registerAll(*blocks.toTypedArray())

        blocks.forEach {
            if (it is BlockTileEntity<*>) {
                // TODO Reimplement Tile Entity registration
                //GameRegistry.registerTileEntity(it.tileEntityClass, it.registryName.toString())
            }
        }
    }

    @SubscribeEvent
    fun registerItems(event: RegistryEvent.Register<Item>) {
        event.registry.registerAll(*items.toTypedArray())
    }

}