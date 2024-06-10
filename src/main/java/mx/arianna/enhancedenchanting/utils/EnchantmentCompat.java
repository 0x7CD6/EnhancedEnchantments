package mx.arianna.enhancedenchanting.utils;

import org.bukkit.enchantments.Enchantment;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentCompat {
    private static final Map<String, Enchantment> ENCHANTMENTS = new HashMap<>();

    static {
        // Try to load enchantments dynamically
        loadEnchantment("PROTECTION_ENVIRONMENTAL", "PROTECTION");
        loadEnchantment("FIRE_PROTECTION", "FIRE_PROTECTION");
        loadEnchantment("FEATHER_FALLING", "FEATHER_FALLING");
        loadEnchantment("BLAST_PROTECTION", "BLAST_PROTECTION");
        loadEnchantment("PROJECTILE_PROTECTION", "PROJECTILE_PROTECTION");
        loadEnchantment("RESPIRATION", "RESPIRATION");
        loadEnchantment("AQUA_AFFINITY", "AQUA_AFFINITY");
        loadEnchantment("THORNS", "THORNS");
        loadEnchantment("DEPTH_STRIDER", "DEPTH_STRIDER");
        loadEnchantment("FROST_WALKER", "FROST_WALKER");
        loadEnchantment("BINDING_CURSE", "BINDING_CURSE");
        loadEnchantment("DAMAGE_ALL", "SHARPNESS");
        loadEnchantment("DAMAGE_UNDEAD", "SMITE");
        loadEnchantment("DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPODS");
        loadEnchantment("KNOCKBACK", "KNOCKBACK");
        loadEnchantment("FIRE_ASPECT", "FIRE_ASPECT");
        loadEnchantment("LOOT_BONUS_MOBS", "LOOTING");
        loadEnchantment("SWEEPING_EDGE", "SWEEPING_EDGE");
        loadEnchantment("DIG_SPEED", "EFFICIENCY");
        loadEnchantment("SILK_TOUCH", "SILK_TOUCH");
        loadEnchantment("DURABILITY", "UNBREAKING");
        loadEnchantment("LOOT_BONUS_BLOCKS", "FORTUNE");
        loadEnchantment("ARROW_DAMAGE", "POWER");
        loadEnchantment("ARROW_KNOCKBACK", "PUNCH");
        loadEnchantment("ARROW_FIRE", "FLAME");
        loadEnchantment("ARROW_INFINITE", "INFINITY");
        loadEnchantment("LUCK", "LUCK_OF_THE_SEA");
        loadEnchantment("LURE", "LURE");
        loadEnchantment("MENDING", "MENDING");
        loadEnchantment("VANISHING_CURSE", "VANISHING_CURSE");
    }

    private static void loadEnchantment(String... names) {
        for (String name : names) {
            try {
                Field field = Enchantment.class.getField(name);
                Enchantment enchantment = (Enchantment) field.get(null);
                ENCHANTMENTS.put(name, enchantment);
                return;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Ignore and try next name
            }
        }
    }

    public static Enchantment getEnchantment(String name) {
        return ENCHANTMENTS.get(name);
    }
}
