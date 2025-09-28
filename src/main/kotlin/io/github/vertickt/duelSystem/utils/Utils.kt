package io.github.vertickt.duelSystem.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.kyori.adventure.translation.GlobalTranslator
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * Returns a [Component] from a [String]
 */
fun String.toComponent(): Component = Component.text(this)

/**
 * Returns a [String] with legacy formatting from a [Component]
 *
 * Note: Render [TranslatableComponent]s before using this
 */
fun Component.toLegacyString(): String = LegacyComponentSerializer.legacy('§').serialize(this)

/**
 * Returns a [String] from a [Component]
 *
 * Note: Render [TranslatableComponent]s before using this
 */
fun Component.plainText(): String = PlainTextComponentSerializer.plainText().serialize(this)

/**
 * Renders a [TranslatableComponent] with the given [locale]
 */
fun TranslatableComponent.render(locale: Locale): Component = GlobalTranslator.render(this, locale)

/**
 * Create a basic [Component] with optional styles. By default, every style is deactivated and don't stack with previous ones!
 */
fun cmp(
    text: String,
    color: TextColor = NamedTextColor.WHITE,
    bold: Boolean = false,
    italic: Boolean = false,
    strikethrough: Boolean = false,
    underlined: Boolean = false
): Component =
    Component.text(text).color(color)
        .decorations(
            mapOf(
                TextDecoration.BOLD to TextDecoration.State.byBoolean(bold),
                TextDecoration.ITALIC to TextDecoration.State.byBoolean(italic),
                TextDecoration.STRIKETHROUGH to TextDecoration.State.byBoolean(strikethrough),
                TextDecoration.UNDERLINED to TextDecoration.State.byBoolean(underlined)
            )
        )

/**
 * Append two components
 * @see Component.append
 */
operator fun Component.plus(other: Component): Component = append(other)

/**
 * Shortcut to add a hover text
 */
fun Component.addHover(display: Component): Component = hoverEvent(asHoverEvent().value(display))

/**
 * Shortcut to add an url [ClickEvent]
 */
fun Component.addUrl(url: String): Component = clickEvent(ClickEvent.openUrl(url))

/**
 * Shortcut to add a run command [ClickEvent]
 */
fun Component.addCommand(command: String): Component = clickEvent(ClickEvent.runCommand(command))

/**
 * Shortcut to add a suggestion [ClickEvent]
 */
fun Component.addSuggest(suggestion: String): Component = clickEvent(ClickEvent.suggestCommand(suggestion))

/**
 * Shortcut to add a copy [ClickEvent]
 */
fun Component.addCopy(copyPrompt: String): Component = clickEvent(ClickEvent.copyToClipboard(copyPrompt))

/**
 * Empty [Component] containing a single whitespace. Useful to bypass auto stripping
 */
fun emptyComponent() = Component.text(" ")

/**
 * Bulk decorate an existing [Component]
 */
fun Component.decorate(
    bold: Boolean? = null,
    italic: Boolean? = null,
    strikethrough: Boolean? = null,
    underlined: Boolean? = null,
    obfuscated: Boolean? = null
): Component =
    apply {
        bold?.let { decoration(TextDecoration.BOLD, it) }
        italic?.let { decoration(TextDecoration.ITALIC, it) }
        strikethrough?.let { decoration(TextDecoration.STRIKETHROUGH, it) }
        underlined?.let { decoration(TextDecoration.UNDERLINED, it) }
        obfuscated?.let { decoration(TextDecoration.OBFUSCATED, it) }
    }


fun resetPlayer(player: Player) {
    player.inventory.clear()
    player.inventory.armorContents = emptyArray<ItemStack?>()
    player.enderChest.clear()
    player.isInvisible = false
    player.isCollidable = true
    player.isSilent = false
    player.canPickupItems = true
    player.health = player.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
    player.foodLevel = 20
    player.saturation = 5f
    player.fireTicks = 0
    player.exp = 0f
    player.level = 0
    player.fallDistance = 0f
    player.activePotionEffects.forEach { effect ->
        player.removePotionEffect(effect.type)
    }
    player.isInvulnerable = false
}