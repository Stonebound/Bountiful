package ejektaflex.bountiful.api.ext

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.ModList

val ItemStack.toNBT: NBTTagCompound
    get() {
        return NBTTagCompound().apply {
            setString("e_item", toPretty)
            setInt("e_amt", count)
            setTag("e_nbt", serializeNBT())
        }
    }

val NBTTagCompound.toItemStack: ItemStack?
    get() {
        val istack = getString("e_item").toItemStack
        return istack?.apply {
            count = getInt("e_amt")
            tag = getTag("e_nbt") as NBTTagCompound
        }
    }

val ItemStack.modOriginName: String?
    get() {
        val modid = item.registryName?.namespace
        return if (modid != null) {
            ModList.get().mods.find { it.modId == modid }?.displayName
        } else {
            null
        }
    }
