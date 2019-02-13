package ejektaflex.bountiful.data

import ejektaflex.bountiful.api.data.IDecree
import ejektaflex.bountiful.api.logic.picked.IPickedEntry

class Decree : IDecree {
    override val decreeTitle = "UNKNOWN"
    override val decreeDescription = "UNKNOWN_DESC"
    override val spawnsInBoard = false
    override val objectives = mutableListOf<IPickedEntry>()
    override val rewards = mutableListOf<IPickedEntry>()
}