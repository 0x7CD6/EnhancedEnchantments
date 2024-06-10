package mx.arianna.enhancedenchanting.utils;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
    public static Map<String, Enchantment> loadEnchantments() {
        Map<String, Enchantment> enchantments = new HashMap<>();
        try {
            Class<?> enchantmentClass = Enchantment.class;
            Field[] fields = enchantmentClass.getDeclaredFields();
            for (Field field : fields) {
                if (Enchantment.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    Enchantment enchantment = (Enchantment) field.get(null);
                    if (enchantment != null) {
                        enchantments.put(field.getName(), enchantment);
                    }
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to load enchantments: " + e.getMessage());
        }
        return enchantments;
    }
}
