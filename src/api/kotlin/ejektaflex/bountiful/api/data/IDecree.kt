package ejektaflex.bountiful.api.data

import ejektaflex.bountiful.api.logic.picked.IPickedEntry

interface IDecree {
    val decreeTitle: String
    val decreeDescription: String
    val spawnsInBoard: Boolean
    val objectives: MutableList<IPickedEntry>
    val rewards: MutableList<IPickedEntry>
}