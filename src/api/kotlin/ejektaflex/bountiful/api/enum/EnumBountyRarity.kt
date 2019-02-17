package ejektaflex.bountiful.api.enum

import ejektaflex.bountiful.api.BountifulAPI
import net.minecraft.item.EnumRarity

enum class EnumBountyRarity(
        val level: Int,
        val itemRarity: EnumRarity,
        //val stat: BountifulStats.BountifulStat,
        val bountyMult: Double = BountifulAPI.config.rarityMultipliers[level],
        val xp: Int = BountifulAPI.config.xpBonuses[level]
) {

    Common(0, EnumRarity.COMMON),
    Uncommon(1, EnumRarity.UNCOMMON),
    Rare(2, EnumRarity.RARE),
    Epic(3, EnumRarity.EPIC);

    companion object {
        fun getRarityFromInt(n: Int): EnumBountyRarity {
            return if (n in 0 until values().size) {
                EnumBountyRarity.values()[n]
            } else {
                Common
            }
        }
    }
}

