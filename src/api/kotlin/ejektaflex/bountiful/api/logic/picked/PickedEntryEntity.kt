package ejektaflex.bountiful.api.logic.picked

import com.google.gson.annotations.Expose
import ejektaflex.bountiful.api.BountifulAPI
import ejektaflex.bountiful.api.ext.toEntityEntry
import net.minecraft.client.resources.I18n
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.registry.EntityEntry

data class PickedEntryEntity(
        @Expose(serialize = false, deserialize = false)
        val genericPick: PickedEntry
) : IPickedEntry by genericPick {

    override fun timeMult() = BountifulAPI.config.entityTimeMult

    var killedAmount = 0

    override fun deserializeNBT(tag: NBTTagCompound) {
        genericPick.deserializeNBT(tag)
        killedAmount = tag.getInteger("killedAmount")
    }

    override fun serializeNBT(): NBTTagCompound {
        return genericPick.serializeNBT().apply {
            setInteger("killedAmount", killedAmount)
        }
    }

    val entityEntry: EntityEntry?
        get() {
            return content.toEntityEntry
        }

    override val contentObj: Any?
        get() = entityEntry

    override val prettyContent: String
        get() = ("($killedAmount/$unitWorth) §a" + I18n.format("entity." + entityEntry?.name + ".name") + " Kills§r")

    override fun toString(): String {
        return "PickedEntry (Entity) [Entity: $content, Amount: $unitWorth, Weight: $weight]"
    }

    override fun isValid(): Boolean {
        return contentObj != null
    }

}