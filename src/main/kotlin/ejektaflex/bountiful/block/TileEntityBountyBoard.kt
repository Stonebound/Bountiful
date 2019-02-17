package ejektaflex.bountiful.block


import ejektaflex.bountiful.Bountiful
import ejektaflex.bountiful.api.block.ITileEntityBountyBoard
import ejektaflex.bountiful.api.ext.clear
import ejektaflex.bountiful.cap.CapManager
import ejektaflex.bountiful.cap.IGlobalBoard
import ejektaflex.bountiful.logic.BountyHolder
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.ItemStackHandler
import java.lang.reflect.Type
import java.util.function.Supplier
import kotlin.math.max


class TileEntityBountyBoard : TileEntity(
        TileEntityType.CHEST // TODO Create TileEntity type
), ITileEntityBountyBoard {

    private val internalInv = BountyHolder(ItemStackHandler(numSlots))

    private val isLocalBounties: Boolean
        get() = !Bountiful.config.globalBounties

    private val cap: IGlobalBoard?
        get() = world.getCapability(CapManager.CAP_BOARD!!, null).orElse(null)

    override val inventory: BountyHolder
        get() {
            return if (!isLocalBounties) {
                if (world.getCapability(CapManager.CAP_BOARD!!, null).isPresent) {
                    cap!!.holder
                } else {
                    internalInv
                }
            } else {
                internalInv
            }
        }

    override var newBoard = true

    var pulseLeft = 0


    override fun write(tag: NBTTagCompound): NBTTagCompound {
        if (isLocalBounties) {
            tag.clear()
            tag.setTag("inv", inventory.handler.serializeNBT())
            tag.setBoolean("newBoard", newBoard)
            tag.setInt("pulseLeft", pulseLeft)
        }
        return super.write(tag)
    }

    override fun read(tag: NBTTagCompound) {
        if (isLocalBounties) {
            inventory.handler.deserializeNBT(tag.getTag("inv") as NBTTagCompound)
            newBoard = tag.getBoolean("newBoard")
            pulseLeft = tag.getInt("pulseLeft")
        }
        super.read(tag)
    }

    private fun updatePulse() {
        val oldPulse = pulseLeft
        pulseLeft = max(pulseLeft - 1, 0)
        if (pulseLeft != oldPulse) {
            val blockstate = world.getBlockState(pos)
            world.notifyNeighborsOfStateChange(pos, blockstate.block)
            //world.notifyBlockUpdate(pos, blockstate, blockstate, 0b111)
        }
    }

    override fun sendRedstonePulse() {
        pulseLeft += 2 // 2 ticks, a standard minimum pulse length
        updatePulse()
    }

    override fun tick() {
        if (!world.isRemote && isLocalBounties) {
            // Skip placement update tick
            if (newBoard) {
                newBoard = false
                return
            }
            // Pulse length update
            updatePulse()

            // Only do tile updates if it's a local inventory
            val dirty = inventory.update(world, this)
            if (dirty) {
                markDirty()
            }
        }
    }

    // No more hasCapability
    /*
    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)
    }
    */

    @Suppress("UNCHECKED_CAST")
    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): LazyOptional<T> {
        return LazyOptional.of {
            if (capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) inventory.handler else {
                super.getCapability<T>(capability, facing)
            }
        } as LazyOptional<T>
    }

    companion object {
        const val numSlots = 27
    }

}
