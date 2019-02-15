package ejektaflex.bountiful.api.data

import ejektaflex.bountiful.api.generic.IUniqueId
import ejektaflex.bountiful.api.logic.picked.IPickedEntry
import ejektaflex.bountiful.api.registry.IValueRegistry

interface IEntryPool : IValueRegistry<IPickedEntry>, IUniqueId