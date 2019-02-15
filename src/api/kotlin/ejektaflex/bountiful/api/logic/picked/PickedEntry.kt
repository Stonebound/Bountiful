package ejektaflex.bountiful.api.logic.picked

import com.google.gson.annotations.SerializedName
import ejektaflex.bountiful.api.data.IHasTag
import ejektaflex.bountiful.api.ext.getUnsortedStringSet
import ejektaflex.bountiful.api.ext.setUnsortedStringSet
import ejektaflex.bountiful.api.logic.ItemRange
import net.minecraft.nbt.*

open class PickedEntry(
        override var content: String = "UNDEFINED",
        override var unitWorth: Int = Integer.MIN_VALUE,
        override var weight: Int = 100,
        @SerializedName("nbt_data")
        override var nbtJson: String? = null,
        override var stages: MutableList<String>? = null,
        override var amountRange: ItemRange? = null,
        var amount: Int = amountRange?.min ?: 1
) : IPickedEntry, IHasTag {

    val randCount: Int
        get() = ((amountRange?.min ?: 1)..(amountRange?.max ?: Int.MAX_VALUE)).random()

    // Must override because overriding [nbtJson]
    override val tag: NBTTagCompound?
        get() = super.tag

    override fun timeMult() = 1.0

    override fun serializeNBT(): NBTTagCompound {
        return NBTTagCompound().apply {
            setString("content", content)
            setInteger("unitWorth", unitWorth)
            tag?.let {
                setTag("nbt", it)
            }
            stages?.let { setUnsortedStringSet("stages", it.toSet()) }
        }
    }

    fun pick() {
        amount = randCount
    }

    override fun deserializeNBT(tag: NBTTagCompound) {
        content = tag.getString("content")
        unitWorth = tag.getInteger("unitWorth")
        if (tag.hasKey("nbt")) {
            nbtJson = tag.getCompoundTag("nbt").toString()
        }
        stages = tag.getUnsortedStringSet("stages").toMutableList()
    }

    override fun toString() = "Picked(§f${unitWorth}x §6$content)"

    override val prettyContent: String
        get() = toString()

    override fun typed(): IPickedEntry {
        if (":" in content) {
            return when (val type = content.substringBefore(':')) {
                "entity" -> PickedEntryEntity(this)
                else -> PickedEntryStack(this)
            }
        } else {
            throw Exception("Entry: '$content' is has no colon prefix!")
        }
    }

    override val contentObj: Any? = null

    override fun isValid(): Boolean {
        val isNBTValid = (nbtJson == null) || (tag != null)
        return contentObj != null && isNBTValid
    }

}