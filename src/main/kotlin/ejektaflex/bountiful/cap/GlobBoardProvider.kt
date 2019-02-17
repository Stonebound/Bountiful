package ejektaflex.bountiful.cap

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional
import javax.annotation.Nullable


class GlobBoardProvider : ICapabilitySerializable<NBTTagCompound> {

    private val inst = GlobalBoard()

    override fun serializeNBT(): NBTTagCompound {
        return CapManager.CAP_BOARD?.storage!!.writeNBT(CapManager.CAP_BOARD, inst, EnumFacing.UP) as NBTTagCompound
    }

    override fun deserializeNBT(tag: NBTTagCompound) {
        CapManager.CAP_BOARD?.storage!!.readNBT(CapManager.CAP_BOARD, inst, EnumFacing.UP, tag)
    }

    // Return capability instance
    override fun <T> getCapability(capability: Capability<T>, @Nullable facing: EnumFacing?): LazyOptional<T> {
        // TODO Reimplement GlobBoardProvider capability fetching
        return LazyOptional.empty()
    }



}