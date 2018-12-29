package ejektaflex.bountiful.config

import ejektaflex.bountiful.Bountiful
import ejektaflex.bountiful.data.SaveWrapper
import ejektaflex.bountiful.api.logic.pickable.PickableEntry
import ejektaflex.bountiful.registry.ValueRegistry
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ejektaflex.bountiful.api.logic.pickable.PickedEntry
import ejektaflex.bountiful.api.logic.pickable.PickedEntryStack
import ejektaflex.bountiful.api.registry.IValueRegistry
import ejektaflex.bountiful.registry.BountyRegistry
import ejektaflex.bountiful.registry.RewardRegistry
import java.io.File
import java.lang.reflect.Type

object BountifulIO {

    fun ensureDirectory(base: File, name: String): File {
        val newDir = File(base, name)
        if (!newDir.exists()) {
            newDir.mkdirs()
        }
        return newDir
    }

    fun populateConfigFolder(folder: File, defaultData: List<Any>, fileName: String): File {
        val gson = GsonBuilder().setPrettyPrinting().create()
        // Populate entries, fill if none exist
        val fileToPopulate = File(folder, fileName)
        if (!fileToPopulate.exists()) {
            fileToPopulate.apply {
                createNewFile()
                println("Going to serialize content..")
                val content = gson.toJson(defaultData.toTypedArray())
                //println("Content: $content")
                writeText(content)
            }
        }
        return fileToPopulate
    }


    fun hotReloadBounties(fileName: String): List<PickableEntry> {
        val invalids = mutableListOf<PickableEntry>()
        val picked = Gson().fromJson(
                File(Bountiful.configDir, fileName).readText(),
                Array<PickableEntry>::class.java
        ).toList()
        BountyRegistry.empty()
        for (item in picked) {
            if (item.isValid) {
                BountyRegistry.add(item)
            } else {
                invalids.add(item)
            }
        }
        return invalids
    }

    fun hotReloadRewards(fileName: String): List<PickedEntryStack> {
        val invalids = mutableListOf<PickedEntryStack>()
        val picked = Gson().fromJson(
                File(Bountiful.configDir, fileName).readText(),
                Array<PickedEntry>::class.java
        ).map { PickedEntryStack(it) }
        RewardRegistry.empty()
        for (item in picked) {
            if (item.content != null) {
                RewardRegistry.add(item)
            } else {
                invalids.add(item)
            }
        }
        return invalids
    }

}