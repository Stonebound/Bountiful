package ejektaflex.bountiful.registry

import ejektaflex.bountiful.api.registry.IValueRegistry

open class ValueRegistry<T : Any> : IValueRegistry<T> {

    override val content = mutableListOf<T>()

    override fun add(vararg items: T) {
        for (item in items) {
            this.content.add(item)
        }
    }

    override fun remove(item: T) = content.remove(item)

    override fun backup(): List<T> {
        return mutableListOf<T>().apply { addAll(content) }
    }

    override fun restore(backupList: List<T>) {
        empty()
        content.addAll(backupList)
    }

    override fun empty() {
        content.clear()
    }


    override fun replace(newItems: List<T>, condition: T.() -> Boolean): List<T> {
        val invalids = mutableListOf<T>()
        val dataBackup = backup()
        this.empty()
        for (item in newItems) {
            if (condition(item)) {
                this.add(item)
            } else {
                invalids.add(item)
            }
        }
        // If there was any invalid data, toss it and restore the old, valid data
        if (invalids.isNotEmpty()) {
            restore(dataBackup)
        }
        return invalids
    }

}


