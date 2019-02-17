package ejektaflex.bountiful.api.logic.picked

import com.google.gson.annotations.Expose
import ejektaflex.bountiful.api.BountifulAPI
import ejektaflex.bountiful.api.ext.toEntityEntry
import net.minecraft.client.resources.I18n
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NBTTagCompound

data class PickedEntryEntity(
        @Expose(serialize = false, deserialize = false)
        val genericPick: PickedEntry
) : IPickedEntry by genericPick {

    override fun timeMult() = BountifulAPI.config.entityTimeMult

    var killedAmount = 0

    override fun deserializeNBT(tag: NBTTagCompound) {
        genericPick.deserializeNBT(tag)
        killedAmount = tag.getInt("killedAmount")
    }

    override fun serializeNBT(): NBTTagCompound {
        return genericPick.serializeNBT().apply {
            setInt("killedAmount", killedAmount)
        }
    }

    val entityEntry: EntityType<*>?
        get() {
            return content.toEntityEntry
        }

    override val contentObj: Any?
        get() = entityEntry

    override val prettyContent: String
        get() = ("($killedAmount/$amount) §a" + I18n.format("entity." + entityEntry?.registryName + ".name") + " Kills§r")

    override fun toString(): String {
        return "PickedEntry (Entity) [Entity: $content, Amount: $amount, Weight: $weight]"
    }

    override fun isValid(): Boolean {
        return contentObj != null
    }

}