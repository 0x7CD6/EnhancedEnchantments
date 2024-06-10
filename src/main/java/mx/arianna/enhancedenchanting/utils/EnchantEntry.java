package mx.arianna.enhancedenchanting.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;

@Getter
@RequiredArgsConstructor
public class EnchantEntry {
    private final Enchantment enchantment;
    private final int level;
}
