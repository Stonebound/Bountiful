package ejektaflex.bountiful.proxy

import ejektaflex.bountiful.Bountiful
import ejektaflex.bountiful.BountifulInfo
import ejektaflex.bountiful.api.events.PopulateBountyBoardEvent
import ejektaflex.bountiful.api.ext.ifHasCapability
import ejektaflex.bountiful.cap.*
import ejektaflex.bountiful.config.BountifulIO
import ejektaflex.bountiful.data.DefaultData
import ejektaflex.bountiful.item.ItemBounty
import ejektaflex.bountiful.logic.BountyChecker
import ejektaflex.bountiful.registry.BountyRegistry
import ejektaflex.bountiful.registry.RewardRegistry
import ejektaflex.bountiful.api.stats.BountifulStats
import ejektaflex.bountiful.data.BountyData
import ejektaflex.bountiful.logic.BountyCreator
import ejektaflex.bountiful.worldgen.VillageBoardComponent
import ejektaflex.bountiful.worldgen.VillageBoardCreationHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.registry.VillagerRegistry
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent

open class CommonProxy : IProxy {







}