package mx.arianna.enhancedenchanting.utils;

import mx.arianna.enhancedenchanting.enchants.CustomEnchantment;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;

public class EnhancedUtils {
    public static class ChatColors {
        public static final String
                MAGIC = ChatColor.MAGIC.toString(),
                BOLD = ChatColor.BOLD.toString(),
                STRIKETHROUGH = ChatColor.STRIKETHROUGH.toString(),
                UNDERLINE = ChatColor.UNDERLINE.toString(),
                ITALIC = ChatColor.ITALIC.toString(),
                RESET = ChatColor.RESET.toString(),
                BLACK = ChatColor.BLACK.toString(),
                DARK_BLUE = ChatColor.DARK_BLUE.toString(),
                DARK_GREEN = ChatColor.DARK_GREEN.toString(),
                DARK_AQUA = ChatColor.DARK_AQUA.toString(),
                DARK_RED = ChatColor.DARK_RED.toString(),
                DARK_PURPLE = ChatColor.DARK_PURPLE.toString(),
                GOLD = ChatColor.GOLD.toString(),
                GRAY = ChatColor.GRAY.toString(),
                DARK_GRAY = ChatColor.DARK_GRAY.toString(),
                BLUE = ChatColor.BLUE.toString(),
                GREEN = ChatColor.GREEN.toString(),
                AQUA = ChatColor.AQUA.toString(),
                RED = ChatColor.RED.toString(),
                LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString(),
                YELLOW = ChatColor.YELLOW.toString(),
                WHITE = ChatColor.WHITE.toString();

        public static final String PRIMARY = GOLD;
        public static final String SECONDARY = GRAY;
        public static final String ERROR = RED;
        public static final String SUCCESS = GREEN;
    }

    public static Enchantment getEnchantmentByName(String name) {
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.getKey().getKey().equalsIgnoreCase(name)) {
                return enchantment;
            }
        }
        return null;
    }

    public static String formatCondition(boolean condition, String message) {
        return (condition ? ChatColors.GREEN : ChatColors.RED) + message;
    }

    public static <T> String formatItem(T itemName) {
        return ChatColors.LIGHT_PURPLE + itemName.toString() + ChatColors.GRAY;
    }
}
