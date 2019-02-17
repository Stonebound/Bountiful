package ejektaflex.bountiful.cap

import net.minecraft.nbt.INBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability

class Storage : Capability.IStorage<IGlobalBoard> {

    override fun writeNBT(capability: Capability<IGlobalBoard>, instance: IGlobalBoard, side: EnumFacing): INBTBase {
        return instance.serializeNBT()
    }

    override fun readNBT(capability: Capability<IGlobalBoard>, instance: IGlobalBoard, side: EnumFacing, nbt: INBTBase) {
        instance.deserializeNBT(nbt as NBTTagCompound)
    }

}
