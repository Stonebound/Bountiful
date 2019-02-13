package ejektaflex.bountiful.data

import ejektaflex.bountiful.api.data.IEntryPool
import ejektaflex.bountiful.api.logic.picked.IPickedEntry

class EntryPool(override val id: String, override val entries: MutableList<IPickedEntry> = mutableListOf()) : IEntryPool {

}