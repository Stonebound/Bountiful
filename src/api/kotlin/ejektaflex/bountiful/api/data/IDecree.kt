package ejektaflex.bountiful.api.data

import ejektaflex.bountiful.api.generic.IUniqueId
import ejektaflex.bountiful.api.logic.picked.IPickedEntry

interface IDecree : IUniqueId {
    val decreeTitle: String
    val isGreedy: Boolean
    val decreeDescription: String
    val spawnsInBoard: Boolean
    val spawnsInWorld: Boolean
    val objectivePools: MutableList<String>
    val rewardPools: MutableList<String>
    val objectives: List<IPickedEntry>
    val rewards: List<IPickedEntry>
}