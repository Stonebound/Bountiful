package ejektaflex.bountiful.data

import ejektaflex.bountiful.api.data.IEntryPool
import ejektaflex.bountiful.registry.EntryRegistry

class EntryPool(override val id: String) : EntryRegistry(), IEntryPool {

    enum class EnumPoolType {
        OBJECTIVE,
        REWARD
    }

}