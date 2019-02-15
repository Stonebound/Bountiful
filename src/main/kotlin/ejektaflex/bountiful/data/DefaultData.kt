package ejektaflex.bountiful.data

import ejektaflex.bountiful.api.ext.ir
import ejektaflex.bountiful.api.logic.ItemRange
import ejektaflex.bountiful.api.logic.picked.PickedEntry
import ejektaflex.bountiful.api.logic.picked.PickedEntryStack
import ejektaflex.bountiful.registry.ValueRegistry

object DefaultData {

    val decrees = ValueRegistry<Decree>().apply {
        add(
                Decree(
                        "Village Rations: Food",
                        "The people are in need of food!",
                        "food",
                        spawnsInBoard = true,
                        objectivePools = mutableListOf(
                                "vanilla_food_bounties"
                        ),
                        rewardPools = mutableListOf(
                                "vanilla_food_rewards",
                                "money_rewards"
                        )
                )
        )
    }

    val pools = ValueRegistry<EntryPool>().apply {
        add(
                EntryPool("vanilla_food_bounties").apply {
                    add(
                            PickedEntry("minecraft:fish", 80, amountRange = (2..32).ir),
                            PickedEntry("minecraft:apple", 70, amountRange = (2..32).ir),
                            PickedEntry("minecraft:bread", 60, amountRange = (1..12).ir),
                            PickedEntry("minecraft:sugar", 55, amountRange = (1..64).ir),
                            PickedEntry("minecraft:wheat", 20, amountRange = (1..48).ir)
                    )
                },

                EntryPool("vanilla_food_rewards").apply {
                    add(
                            PickedEntry("minecraft:cake", 200, amountRange = (1..3).ir),
                            PickedEntry("minecraft:baked_potato", 120, amountRange = (4..24).ir)
                    )
                },

                EntryPool("money_rewards").apply {
                    add(
                            PickedEntryStack(PickedEntry("minecraft:gold_nugget", 100)),
                            PickedEntryStack(PickedEntry("minecraft:gold_ingot", 900))
                    )
                }

        )
    }

    val entries = ValueRegistry<PickedEntry>().apply {
        add(
            PickedEntry("minecraft:dirt", 5, amountRange = (16..128).ir),
            PickedEntry("minecraft:stone", 10, amountRange = (16..128).ir),
            PickedEntry("minecraft:cobblestone", 7, amountRange = (16..128).ir),
            PickedEntry("minecraft:fish", 80, amountRange = (2..32).ir),
            PickedEntry("minecraft:apple", 70, amountRange = (2..32).ir),
            PickedEntry("minecraft:book", 80, amountRange = (2..16).ir),
            PickedEntry("minecraft:cactus", 80, amountRange = (2..32).ir),
            PickedEntry("minecraft:diamond", 1600, amountRange = (1..8).ir),
            PickedEntry("minecraft:dispenser", 200, amountRange = (1..6).ir),
            PickedEntry("minecraft:iron_ingot", 200, amountRange = (1..32).ir),
            PickedEntry("minecraft:bread", 60, amountRange = (1..12).ir),
            PickedEntry("minecraft:rotten_flesh", 55, amountRange = (1..24).ir),
            PickedEntry("minecraft:sign", 65, amountRange = (1..16).ir),
            PickedEntry("minecraft:slime_ball", 60, amountRange = (1..32).ir),
            PickedEntry("minecraft:spider_eye", 75, amountRange = (1..16).ir),
            PickedEntry("minecraft:string", 35, amountRange = (1..24).ir),
            PickedEntry("minecraft:sugar", 55, amountRange = (1..64).ir),
            PickedEntry("minecraft:tripwire_hook", 70, amountRange = (1..8).ir),
            PickedEntry("minecraft:wheat", 20, amountRange = (1..48).ir),
            PickedEntry("minecraft:leather", 90, amountRange = (1..12).ir),
            PickedEntry("entity:minecraft:zombie", 120, weight = 300, amountRange = (1..8).ir),
            PickedEntry("entity:minecraft:skeleton", 140, weight = 250, amountRange = (1..6).ir),
            PickedEntry("minecraft:potion", 300, nbtJson = "{Potion: \"minecraft:healing\"}", amountRange = (1..3).ir)
        )
    }

    val rewards = ValueRegistry<PickedEntryStack>().apply {
        add(
                PickedEntryStack(PickedEntry("minecraft:gold_nugget", 100)),
                PickedEntryStack(PickedEntry("minecraft:gold_ingot", 900)),
                PickedEntryStack(PickedEntry("minecraft:diamond", 2400, 10)),
                //PickedEntryStack(PickedEntry("minecraft:potion", 800, 5)),
                PickedEntryStack(PickedEntry("minecraft:iron_sword", 750, 5, nbtJson = "{display:{Lore:[\"Sharper than Usual.\"]},ench:[{id:16,lvl:1}]}", amountRange = ItemRange(1, 1))),
                PickedEntryStack(PickedEntry("minecraft:leather_helmet", 500, 5, amountRange = ItemRange(1, 1))),
                PickedEntryStack(PickedEntry("minecraft:leather_boots", 450, 5, amountRange = ItemRange(1, 1))),
                PickedEntryStack(PickedEntry("minecraft:iron_chestplate", 1800, 5, amountRange = ItemRange(1, 1))),
                PickedEntryStack(PickedEntry("minecraft:golden_chestplate", 1200, 5, amountRange = ItemRange(1, 1))),
                PickedEntryStack(PickedEntry("minecraft:bow", 1300, 10, "{ench:[{id:48,lvl:2}]}", amountRange = ItemRange(1, 1)))
        )
    }

}