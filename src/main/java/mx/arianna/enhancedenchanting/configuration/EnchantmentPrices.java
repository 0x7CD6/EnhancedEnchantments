package mx.arianna.enhancedenchanting.configuration;

import mx.arianna.enhancedenchanting.enchants.CustomEnchantment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class EnchantmentPrices {
    private final File configFile;
    private final FileConfiguration config;

    public EnchantmentPrices(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        loadDefaultPrices();
        validateConfig();
    }

    private void loadDefaultPrices() {
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment != null) {
                int maxLevel = enchantment.getMaxLevel();
                for (int i = 1; i <= maxLevel; i++) {
                    int cost = determineCost(enchantment, i);
                    config.addDefault("enchantment_prices." + enchantment.getKey().getKey() + "." + i, cost);
                }
            }
        }
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void validateConfig() {
        Set<String> validKeys = new HashSet<>();
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment != null) {
                String enchantmentKey = enchantment.getKey().getKey();
                validKeys.add(enchantmentKey);

                int maxLevel = enchantment.getMaxLevel();
                for (int i = 1; i <= maxLevel; i++) {
                    if (!config.contains("enchantment_prices." + enchantmentKey + "." + i)) {
                        int cost = determineCost(enchantment, i);
                        config.set("enchantment_prices." + enchantmentKey + "." + i, cost);
                    }
                }
            }
        }

        // Remove invalid enchantments
        Set<String> configKeys = config.getConfigurationSection("enchantment_prices").getKeys(false);
        for (String key : configKeys) {
            if (!validKeys.contains(key)) {
                config.set("enchantment_prices." + key, null);
            }
        }
        saveConfig();
    }

    public void setPrice(Enchantment enchantment, int level, int price) {
        if (enchantment != null) {
            config.set("enchantment_prices." + enchantment.getKey().getKey() + "." + level, price);
            saveConfig();
        }
    }

    public int getPrice(Enchantment enchantment, int level) {
        if (enchantment != null) {
            return config.getInt("enchantment_prices." + enchantment.getKey().getKey() + "." + level, determineCost(enchantment, level));
        }
        return 0;
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int determineCost(Enchantment enchantment, int level) {
        int maxLevel = enchantment.getMaxLevel();

        // Base cost calculation
        int baseCost = 5 * level * (1 + maxLevel + level);

        // Apply type-based adjustments
        int typeAdjustedCost = applyTypeAdjustment(enchantment, baseCost);

        // Apply random factor to introduce some variability
        int randomFactor = new Random().nextInt(20) - 10; // Random adjustment between -10 and +10

        // Factor in global difficulty setting
        double difficultyMultiplier = getGlobalDifficultyMultiplier();
        int finalCost = (int) ((typeAdjustedCost + randomFactor) * difficultyMultiplier);

        return Math.max(finalCost, 1); // Ensure cost is at least 1
    }

    private int applyTypeAdjustment(Enchantment enchantment, int cost) {
        if (enchantment.isTreasure()) return cost + 50;
        else if (enchantment.isCursed()) return cost - 30;
        return cost;
    }

    private double getGlobalDifficultyMultiplier() {
        return 1.1; // Slight increase in cost for higher difficulty
    }


}
