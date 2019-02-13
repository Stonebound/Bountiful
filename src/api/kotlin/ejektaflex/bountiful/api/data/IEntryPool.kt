package ejektaflex.bountiful.api.data

import ejektaflex.bountiful.api.logic.picked.IPickedEntry

interface IEntryPool {
    val id: String
    val entries: MutableList<IPickedEntry>
}