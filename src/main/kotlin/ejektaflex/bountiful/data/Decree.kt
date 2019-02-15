package ejektaflex.bountiful.data

import ejektaflex.bountiful.api.data.IDecree
import ejektaflex.bountiful.api.logic.picked.IPickedEntry
import ejektaflex.bountiful.registry.PoolRegistry

class Decree(
        override val decreeTitle: String = "UNKNOWN",
        override val decreeDescription: String = "UNKNOWN_DESC",
        override val id: String = "unknown",
        override val spawnsInBoard: Boolean = false,
        override val isGreedy: Boolean = false,
        override val objectivePools: MutableList<String> = mutableListOf(),
        override val rewardPools: MutableList<String> = mutableListOf()
) : IDecree {


    override val objectives: List<IPickedEntry>
        get() = getEntriesFromTagList(objectivePools)

    override val rewards: List<IPickedEntry>
        get() = getEntriesFromTagList(rewardPools)

    private fun getEntriesFromTagList(poolTags: MutableList<String>): List<IPickedEntry> {
        return PoolRegistry.content.filter { it.id in poolTags }.map { it.content }.flatten()
    }

}