package ejektaflex.bountiful.api.events

import ejektaflex.bountiful.api.block.ITileEntityBountyBoard
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Cancelable
import net.minecraftforge.eventbus.api.Event

@Cancelable
class PopulateBountyBoardEvent(var stack: ItemStack, var board: ITileEntityBountyBoard?) : Event() {
    companion object {
        fun fireEvent(stack: ItemStack, board: ITileEntityBountyBoard?): PopulateBountyBoardEvent {
            val event = PopulateBountyBoardEvent(stack, board)
            MinecraftForge.EVENT_BUS.post(event)
            return event
        }
    }
}