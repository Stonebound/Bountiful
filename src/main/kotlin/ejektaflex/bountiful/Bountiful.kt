package ejektaflex.bountiful

import ejektaflex.bountiful.api.BountifulAPIProvider
import ejektaflex.bountiful.api.events.PopulateBountyBoardEvent
import ejektaflex.bountiful.cap.CapManager
import ejektaflex.bountiful.cap.GlobBoardProvider
import ejektaflex.bountiful.command.BountyCommand
import ejektaflex.bountiful.config.BountifulIO
import ejektaflex.bountiful.config.ConfigFile
import ejektaflex.bountiful.data.BountyData
import ejektaflex.bountiful.dist.ISidedProxy
import ejektaflex.bountiful.dist.client.ClientProxy
import ejektaflex.bountiful.dist.server.ServerProxy
import ejektaflex.bountiful.proxy.IProxy
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.Logger
import java.io.File
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.network.NetworkRegistry
import java.util.function.Supplier
import ejektaflex.bountiful.gui.GuiHandler
import ejektaflex.bountiful.item.ItemBounty
import ejektaflex.bountiful.logic.BountyChecker
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ExtensionPoint
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.gameevent.TickEvent


//@Mod(modid = BountifulInfo.MODID, name = BountifulInfo.NAME, version = BountifulInfo.VERSION, modLanguageAdapter = BountifulInfo.ADAPTER, dependencies = BountifulInfo.DEPENDS)
@Mod(BountifulInfo.MODID)
@Mod.EventBusSubscriber(modid = BountifulInfo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object Bountiful {


    val eventBus = FMLJavaModLoadingContext.get().modEventBus

    //@JvmStatic val PROXY = DistExecutor.runForDist({{ClientProxy()}}, {{ServerProxy()}})
    val PROXY: ISidedProxy = DistExecutor.runForDist<ISidedProxy>(
            {
                Supplier {
                    ClientProxy()
                }
            },
            {
                Supplier {
                    ServerProxy()
                }
            }
    )

    init {
        PROXY.loadEvents()
        MinecraftForge.EVENT_BUS.register(this)
    }

    // Update mob bounties

    fun entityLivingDeath(e: LivingDeathEvent) {
        if (e.source.trueSource is EntityPlayer) {
            val player = e.source.trueSource as EntityPlayer
            val bountyStacks = player.inventory.mainInventory.filter { it.item is ItemBounty }
            if (bountyStacks.isNotEmpty()) {
                bountyStacks.forEach { stack ->
                    val data = BountyData.from(stack)
                    BountyChecker.tryTakeEntities(player, data, stack, e.entityLiving)
                }
            }
        }
    }

    // Update global bounties
    @SubscribeEvent
    fun onWorldTick(e: TickEvent.WorldTickEvent) {
        if (!e.world.isRemote && Bountiful.config.globalBounties && e.phase == TickEvent.Phase.END) {
            // TODO Look up how to check for capabilities in World
            /*
            e.world.ifHasCapability(CapManager.CAP_BOARD!!) {
                holder.update(e.world, null)
            }
            */
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
        if (!e.stack.hasTag()) {
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

    // TODO Replace Proxy system with Dist
    //@SidedProxy(clientSide = BountifulInfo.CLIENT, serverSide = BountifulInfo.SERVER)
    //@JvmStatic lateinit var proxy: IProxy




    // TODO Replace EventHandlers in main mod class with SubscribeEvent?
    //@Mod.EventHandler
    fun loadAPI(event: FMLCommonSetupEvent) = BountifulAPIProvider.changeAPI(InternalAPI)

    lateinit var logger: Logger
    lateinit var configDir: File
    lateinit var config: ConfigFile

    // TODO Provide alternate way of grabbing the Mod's Instance
    //@Mod.Instance
    var instance: Bountiful? = this

    //@Mod.EventHandler
    /*
    override fun preInit(e: FMLPreInitializationEvent) {
        logger = e.modLog
        configDir = BountifulIO.ensureDirectory(e.modConfigurationDirectory, BountifulInfo.MODID)
        config = ConfigFile(configDir)
        config.load()
        MinecraftForge.EVENT_BUS.register(ContentRegistry)
        // TODO Register GuiHandlers some other way
        //NetworkRegistry.INSTANCE.registerGuiHandler(instance, GuiHandler())
        //NetworkRegistry.
        MinecraftForge.EVENT_BUS.register(proxy)
        proxy.preInit(e)
    }
    */

    /*
    @Mod.EventHandler
    override fun init(e: FMLInitializationEvent) {
        proxy.init(e)
    }

    @Mod.EventHandler
    override fun postInit(e: FMLPostInitializationEvent) {
        proxy.postInit(e)
        if (config.hasChanged()) {
            config.save()
        }
    }
    */

    // TODO Register Server commands using new method
    //@Mod.EventHandler
    //fun serverLoad(e: FMLServerStartingEvent) = e.registerServerCommand(BountyCommand())

}
