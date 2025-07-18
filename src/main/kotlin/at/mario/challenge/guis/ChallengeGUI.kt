package at.mario.challenge.guis

import at.mario.challenge.challenges.Challenges
import at.mario.challenge.utils.Lang
import at.mario.challenge.utils.Utils
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import kotlin.math.E

/**
 * GUI for selecting a challenge. Supports pagination and displays all available challenges and their status.
 */
object ChallengeGUI {
    private const val CHALLENGES_PER_PAGE = 5
    val playerPages = mutableMapOf<String, Int>()

    /**
     * Opens the challenge selection GUI for the given player and page.
     * @param player The player to open the GUI for
     * @param page The page number to display
     */
    fun open(player: HumanEntity, page: Int = 0) {
        val challenges = Challenges.values()
        val maxPages = challenges.size / CHALLENGES_PER_PAGE

        val inventory = Bukkit.createInventory(null, 27, Lang.translate("challenge_menu_title"))
        val filler = ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
            itemMeta = itemMeta?.apply { displayName(cmp("")) }
        }

        // Set frame
        for (i in 0..8) inventory.setItem(i, filler)
        for (i in inventory.size-9..inventory.size-1) inventory.setItem(i, filler)

        // Back button
        inventory.setItem(9, Utils().createItem(Material.DARK_OAK_DOOR, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("back"))))
        inventory.setItem(10, Utils().createItem(Material.STRUCTURE_VOID, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("back"))))
        inventory.setItem(16, Utils().createItem(Material.STRUCTURE_VOID, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("back"))))

        // Navigation
        if (page > 0) {
            inventory.setItem(9, Utils().createItem(Material.ARROW, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("previous_page"))))
        }
        if (page < maxPages) {
            inventory.setItem(17, Utils().createItem(Material.ARROW, 1, glow = false, unbreakable = false, false, cmp(Lang.translate("next_page"))))
        }

        // Show challenges
        val startIndex = page * CHALLENGES_PER_PAGE
        val endIndex = minOf(startIndex + CHALLENGES_PER_PAGE, challenges.size)

        for (i in startIndex until endIndex) {
            val challenge = challenges[i]
            val item = ItemStack(challenge.icon)
            val meta = item.itemMeta!!
            meta.displayName(challenge.nameComponent)
            meta.lore(
                listOf(
                    challenge.description,
                    cmp(""),
                    cmp("Status: ", KColors.GRAY) + if (challenge.active) cmp("Aktiv", KColors.GREEN) else cmp("Inaktiv", KColors.RED)
                )
            )
            if (challenge.active) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true)
            }
            item.itemMeta = meta
            inventory.addItem(item)
        }

        inventory.setItem(10, ItemStack(Material.AIR))
        inventory.setItem(16, ItemStack(Material.AIR))
        playerPages[player.name] = page
        player.openInventory(inventory)
    }
}
