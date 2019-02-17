package ejektaflex.bountiful.dist.client

import ejektaflex.bountiful.ContentRegistry
import ejektaflex.bountiful.api.enum.EnumBountyRarity
import ejektaflex.bountiful.dist.ISidedProxy
import ejektaflex.bountiful.item.ItemBounty
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class ClientProxy : ISidedProxy {
//@SideOnly(Side.CLIENT)
    // TODO Provide alternative for an ItemMeshDefinition
    /*
    private class BountyItemDefinition(val id: String) : ItemMeshDefinition {
        override fun getModelLocation(stack: ItemStack): ModelResourceLocation {
            val locid = BountifulInfo.MODID + ":" + id
            return try {
                val data = BountifulAPI.toBountyData(stack)
                val rarity = EnumBountyRarity.getRarityFromInt(data.rarity)
                ModelResourceLocation("$locid-${rarity.name.toLowerCase()}", "inventory")
            } catch (e: Exception) {
                ModelResourceLocation("$locid-${EnumBountyRarity.Common.name.toLowerCase()}", "inventory")
            }
        }
    }
    */

    private fun registerItemRenderer(item: Item, meta: Int, id: String) {
        // TODO Replace method for setting custom ModelRL
        //ModelLoader.setCustomModelResourceLocation(item, meta, ModelResourceLocation(BountifulInfo.MODID + ":" + id, "inventory"))
    }

    private fun registerBountyRenderer(item: Item, meta: Int, id: String) {
        // TODO Provide alternate method of registering item variants (This isn't even needed anymore, right? No meta?)
        for (rarity in EnumBountyRarity.values().map { it.name.toLowerCase() }) {
            //ModelBakery.registerItemVariants(item, ResourceLocation(BountifulInfo.MODID + ":" + id + "-$rarity"))
        }
        // TODO Provide alternate method of setting a custom mesh definition
        //ModelLoader.setCustomMeshDefinition(item, BountyItemDefinition(id))
    }

    @SubscribeEvent
    fun registerModels(event: ModelRegistryEvent) {
        ContentRegistry.items.forEach {
            if (it is ItemBounty) {
                registerBountyRenderer(it, 0, it.registryName!!.path)
            } else {
                registerItemRenderer(it, 0, it.registryName!!.path)
            }
        }
    }
}