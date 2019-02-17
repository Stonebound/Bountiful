package ejektaflex.bountiful.dist.server

import ejektaflex.bountiful.Bountiful
import ejektaflex.bountiful.cap.GlobalBoard
import ejektaflex.bountiful.cap.IGlobalBoard
import ejektaflex.bountiful.cap.Storage
import ejektaflex.bountiful.config.BountifulIO
import ejektaflex.bountiful.data.DefaultData
import ejektaflex.bountiful.dist.ISidedProxy
import ejektaflex.bountiful.registry.BountyRegistry
import ejektaflex.bountiful.registry.RewardRegistry
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.common.registry.VillagerRegistry
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent

class ServerProxy : ISidedProxy {

    override val setupCommonEvent = { event: FMLCommonSetupEvent ->
        CapabilityManager.INSTANCE.register(IGlobalBoard::class.java, Storage()) { GlobalBoard() }
        //VillagerRegistry.instance().registerVillageCreationHandler(VillageBoardCreationHandler())
        //MapGenStructureIO.registerStructureComponent(VillageBoardComponent::class.java, VillageBoardComponent.VILLAGE_BOARD_ID.toString())
    }

    override val loadCompleteEvent = { event: FMLLoadCompleteEvent ->
        "bounties.json".let {
            BountifulIO.populateConfigFolder(Bountiful.configDir, DefaultData.entries.items, it)
            val invalids = BountifulIO.hotReloadBounties()
            if (invalids.isNotEmpty()) {
                throw Exception("'bountiful/bounties.json' contains one or more invalid bounties. Invalid bounty objectives: $invalids")
            }
            val minObjectives = Bountiful.config.bountyAmountRange.last
            if (BountyRegistry.items.size < minObjectives) {
                throw Exception("Config file needs more bounties! Must have at least $minObjectives bounty objectives to choose from, according to the current config.")
            }
        }

        // Same for rewards
        "rewards.json".let {
            BountifulIO.populateConfigFolder(Bountiful.configDir, DefaultData.rewards.items.map { item ->
                item.genericPick
            }, it)
            val invalids = BountifulIO.hotReloadRewards()
            if (invalids.isNotEmpty()) {
                throw Exception("'bountiful/rewards.json' contains one or more invalid rewards. Invalid rewards: $invalids")
            }
        }

        //BountifulStats.register()

        println("Bounties: ${BountyRegistry.items.size}")
        BountyRegistry.items.forEach { println(it) }

        println("Rewards: ${RewardRegistry.items.size}")
        RewardRegistry.items.forEach { println(it) }
    }
}