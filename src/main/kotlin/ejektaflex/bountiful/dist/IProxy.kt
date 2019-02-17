package ejektaflex.bountiful.dist

import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

interface IProxy {

    fun loadEvents() {
        setupClientEvent?.let { loadEvent(it) }
        setupCommonEvent?.let { loadEvent(it) }
        loadCompleteEvent?.let { loadEvent(it) }
    }



    fun <T : Event> loadEvent(func: T.() -> Unit) {
        FMLJavaModLoadingContext.get().modEventBus.addListener(func)
    }

    val setupClientEvent: (FMLClientSetupEvent.() -> Unit)?
        get() = null

    val setupCommonEvent: (FMLCommonSetupEvent.() -> Unit)?
        get() = null

    val loadCompleteEvent: (FMLLoadCompleteEvent.() -> Unit)?
        get() = null


}