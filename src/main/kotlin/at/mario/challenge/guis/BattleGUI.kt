package at.mario.challenge.guis

import at.mario.challenge.challenges.Battles
import at.mario.challenge.utils.Lang
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * GUI for selecting a battle mode. Displays all available battle modes and their status.
 */
object BattleGUI {
    /**
     * Opens the battle mode selection GUI for the given player.
     * @param player The player to open the GUI for
     */
    fun open(player: HumanEntity) {
        val inventory: Inventory = Bukkit.createInventory(null, 27, Lang.translate("battle_menu_title"))
        val item1 = ItemStack(Material.GRAY_STAINED_GLASS_PANE)
        val meta1: ItemMeta = item1.itemMeta!!
        meta1.displayName(cmp(""))
        item1.itemMeta = meta1

        for (i in 0..8) {
            inventory.setItem(i, item1)
        }
        for (i in 18..26) {
            inventory.setItem(i, item1)
        }
        inventory.addItem(Utils().createItem(Material.DARK_OAK_DOOR, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("back"))))
        inventory.setItem(10, item1)

        for (battles in Battles.values()) {
            val item = ItemStack(battles.icon)
            val meta: ItemMeta = item.itemMeta!!
            meta.displayName(battles.nameComponent)
            if (battles.active) {
                meta.lore(listOf(battles.description, cmp(""), cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("active"), KColors.GREEN)))
            } else {
                meta.lore(listOf(battles.description, cmp(""), cmp(Lang.translate("status"), KColors.GRAY) + cmp(Lang.translate("inactive"), KColors.RED)))
            }
            item.itemMeta = meta
            inventory.addItem(item)
        }
        inventory.setItem(10, ItemStack(Material.AIR))
        player.openInventory(inventory)
    }
}