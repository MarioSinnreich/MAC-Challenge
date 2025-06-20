package at.mario.challenge.commands

import at.mario.challenge.Main
import at.mario.challenge.challenges.TrySystem
import at.mario.challenge.timer.Timer
import at.mario.challenge.utils.Config
import at.mario.challenge.utils.Lang
import de.miraculixx.kpaper.chat.KColors
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.Bukkit
import kotlin.time.Duration

/**
 * Command for managing challenge attempts ("Trys").
 * Allows starting, resetting, getting, and setting the number of attempts.
 */
class TryCommand {
    /**
     * The try command tree for managing attempts.
     */
    val tryCommand = commandTree("try"){
        literalArgument("start") {
            anyExecutor { _, _ ->
                TrySystem().attempts += 1
                Bukkit.broadcast(Main.prefix + cmp(Lang.translate("try_started", TrySystem().attempts), KColors.LIGHTGRAY))
                Timer.setTime(Duration.ZERO)
                Timer.paused = false
                Timer.hidden = false
            }
        }
        literalArgument("reset") {
            anyExecutor{ _,_ ->
                TrySystem().attempts = 0
                Bukkit.broadcast(Main.prefix + cmp(Lang.translate("try_reset"), KColors.LIGHTGRAY))
            }
        }
        literalArgument("get") {
            anyExecutor { _, _ ->
                Bukkit.broadcast(Main.prefix + cmp(Lang.translate("try_get", TrySystem().attempts), KColors.LIGHTGRAY))
            }
        }
        literalArgument("set") {
            integerArgument("attempts") {
                anyExecutor { _, args ->
                    TrySystem().attempts = args["attempts"] as Int
                    Bukkit.broadcast(Main.prefix + cmp(Lang.translate("try_get", TrySystem().attempts), KColors.LIGHTGRAY))
                }
            }
        }
    }
}