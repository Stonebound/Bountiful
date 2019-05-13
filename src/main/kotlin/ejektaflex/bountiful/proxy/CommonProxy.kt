package ejektaflex.bountiful.proxy

import ejektaflex.bountiful.Bountiful
import ejektaflex.bountiful.BountifulInfo
import ejektaflex.bountiful.api.events.PopulateBountyBoardEvent
import ejektaflex.bountiful.api.ext.ifHasCapability
import ejektaflex.bountiful.api.registry.IValueRegistry
import ejektaflex.bountiful.cap.*
import ejektaflex.bountiful.config.BountifulIO
import ejektaflex.bountiful.data.DefaultData
import ejektaflex.bountiful.item.ItemBounty
import ejektaflex.bountiful.logic.BountyChecker
import ejektaflex.bountiful.registry.BountyRegistry
import ejektaflex.bountiful.registry.RewardRegistry
import ejektaflex.bountiful.api.stats.BountifulStats
import ejektaflex.bountiful.data.BountyEntry
import ejektaflex.bountiful.worldgen.VillageBoardComponent
import ejektaflex.bountiful.worldgen.VillageBoardCreationHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraft.world.gen.structure.MapGenStructureIO
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.registry.VillagerRegistry

open class CommonProxy : IProxy {

    override fun preInit(e: FMLPreInitializationEvent) {
        CapabilityManager.INSTANCE.register(IGlobalBoard::class.java, Storage()) { GlobalBoard() }
        VillagerRegistry.instance().registerVillageCreationHandler(VillageBoardCreationHandler())
        MapGenStructureIO.registerStructureComponent(VillageBoardComponent::class.java, VillageBoardComponent.VILLAGE_BOARD_ID.toString())
    }

    // Update mob bounties
    @SubscribeEvent
    fun entityLivingDeath(e: LivingDeathEvent) {
        if (e.source.trueSource is EntityPlayer) {
            val player = e.source.trueSource as EntityPlayer
            val bountyStacks = player.inventory.mainInventory.filter { it.item is ItemBounty }
            if (bountyStacks.isNotEmpty()) {
                bountyStacks.forEach { stack ->
                    val data = BountyEntry.from(stack)
                    BountyChecker.tryTakeEntities(player, data, stack, e.entityLiving)
                }
            }
        }
    }

    // Update global bounties
    @SubscribeEvent
    fun onWorldTick(e: TickEvent.WorldTickEvent) {
        if (!e.world.isRemote && Bountiful.config.globalBounties && e.phase == TickEvent.Phase.END) {
            e.world.ifHasCapability(CapManager.CAP_BOARD!!) {
                holder.update(e.world, null)
            }
        }
    }

    // Attach global bounty inventory to world
    @SubscribeEvent
    fun attachCaps(e: AttachCapabilitiesEvent<World>) {
        e.addCapability(ResourceLocation(BountifulInfo.MODID, "GlobalData"), GlobBoardProvider())
    }

    @SubscribeEvent
    fun onBoardPost(e: PopulateBountyBoardEvent) {
        // Don't post bounties without data to the bounty board.
        if (!e.stack.hasTagCompound()) {
            e.isCanceled = true
        }
        // Cancel first posting to board on board creation (as first update is immediate after placement)
        e.board?.let {
            if (it.newBoard) {
                it.newBoard = false
                e.isCanceled = true
            }
        }
    }

    override fun postInit(e: FMLPostInitializationEvent) {

        for (decree in DefaultData.decrees.content) {
            BountifulIO.populateConfigFolder(Bountiful.decreeDir, decree, "${decree.id}.json")
        }
3
        for (pool in DefaultData.pools.content) {
            BountifulIO.populateConfigFolder(Bountiful.poolDir, pool, "${pool.id}.json")
        }

        // Populate entries, fill if none exist
        "bounties.json".let {
            BountifulIO.populateConfigFolder(Bountiful.configDir, DefaultData.entries.content.toTypedArray(), it)
            val invalids = BountifulIO.hotReloadBounties()
            if (invalids.isNotEmpty()) {
                throw Exception("'bountiful/bounties.json' contains one or more invalid bounties. Invalid bounty objectivePools: $invalids")
            }
            val minObjectives = Bountiful.config.bountyAmountRange.last
            if (BountyRegistry.content.size < minObjectives) {
                throw Exception("Config file needs more bounties! Must have at least $minObjectives bounty objectivePools to choose from, according to the current config.")
            }
        }

        // Same for rewardPools
        "rewardPools.json".let {
            BountifulIO.populateConfigFolder(Bountiful.configDir, DefaultData.rewards.content.toTypedArray().map { item ->
                item.genericPick
            }, it)
            val invalids = BountifulIO.hotReloadRewards()
            if (invalids.isNotEmpty()) {
                throw Exception("'bountiful/rewardPools.json' contains one or more invalid rewardPools. Invalid rewardPools: $invalids")
            }
        }

        BountifulStats.register()

        fun displayRegistry(reg: IValueRegistry<*>, name: String) {
            println("$name: ${reg.content.size}")
            reg.content.forEach { println(it) }
        }

        displayRegistry(BountyRegistry, "Bounties")
        displayRegistry(RewardRegistry, "Rewards")

    }

}