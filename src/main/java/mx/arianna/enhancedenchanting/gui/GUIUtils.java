package mx.arianna.enhancedenchanting.gui;

import mx.arianna.enhancedenchanting.configuration.EnchantmentPrices;
import mx.arianna.enhancedenchanting.enchants.CustomEnchantment;
import mx.arianna.enhancedenchanting.utils.EnchantEntry;
import mx.arianna.enhancedenchanting.utils.EnhancedUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.*;

public class GUIUtils {

    private final EnchantmentPrices enchantmentPrices;

    public GUIUtils(EnchantmentPrices enchantmentPrices) {
        this.enchantmentPrices = enchantmentPrices;
    }

    public void openInitialGUI(Player player) {
        Inventory initialInventory = Bukkit.createInventory(null, 9, EnhancedUtils.ChatColors.DARK_PURPLE + "Insert Item");
        ItemStack glassPane = createGlassPane(Material.GRAY_STAINED_GLASS_PANE);

        for (int i = 0; i < 9; i++)
            if (i != 4) initialInventory.setItem(i, glassPane);

        player.openInventory(initialInventory);
    }

    public void openEnchantmentGUI(Player player, ItemStack itemToEnchant) {
        List<EnchantEntry> validEnchantments = getValidEnchantments(itemToEnchant);
        int numEnchantments = validEnchantments.size();
        int size = 9;

        if (numEnchantments <= 4) size = 27;
        else if (numEnchantments <= 11) size = 36;
        else if (numEnchantments <= 18) size = 45;
        else if (numEnchantments <= 25) size = 54;

        Inventory enchantmentInventory = Bukkit.createInventory(null, size, EnhancedUtils.ChatColors.DARK_PURPLE + "Enchantment Options");
        setupCustomLayout(enchantmentInventory, createGlassPane(Material.PURPLE_STAINED_GLASS_PANE));
        enchantmentInventory.setItem(4, itemToEnchant);
        addEnchantmentsToGUI(enchantmentInventory, itemToEnchant);
        player.openInventory(enchantmentInventory);
    }

    public static void setupCustomLayout(Inventory inventory, ItemStack glassPane) {
        Set<Integer> emptySlots = Set.of(4, 10, 11, 15, 16);
        for (int i = 0; i < inventory.getSize(); i++)
            if (!emptySlots.contains(i)) inventory.setItem(i, glassPane);
    }

    private void addEnchantmentsToGUI(Inventory inventory, ItemStack itemToEnchant) {
        List<EnchantEntry> validEnchantments = getValidEnchantments(itemToEnchant);
        validEnchantments.sort(Comparator.comparing(entry -> entry.getEnchantment().getKey().getKey()));
        int[] specialPatternSlots = {10, 11, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        int index = 0;
        for (EnchantEntry entry : validEnchantments) {
            Enchantment enchantment = entry.getEnchantment();
            if (enchantment != null && !isCurse(enchantment) && index < specialPatternSlots.length) {
                int currentLevel = itemToEnchant.getEnchantmentLevel(enchantment);
                int displayLevel = currentLevel < enchantment.getMaxLevel() ? currentLevel + 1 : currentLevel;
                inventory.setItem(specialPatternSlots[index], createEnchantmentBook(itemToEnchant, enchantment, displayLevel, enchantment.getMaxLevel()));
                index++;
            }
        }

        for (EnchantEntry entry : validEnchantments) {
            Enchantment enchantment = entry.getEnchantment();
            if (enchantment != null && isCurse(enchantment) && index < specialPatternSlots.length) {
                int currentLevel = itemToEnchant.getEnchantmentLevel(enchantment);
                int displayLevel = currentLevel < enchantment.getMaxLevel() ? currentLevel + 1 : currentLevel;
                inventory.setItem(specialPatternSlots[index], createEnchantmentBook(itemToEnchant, enchantment, displayLevel, enchantment.getMaxLevel()));
                index++;
            }
        }
    }

    private ItemStack createEnchantmentBook(ItemStack itemToEnchant, Enchantment enchantment, int displayLevel, int maxLevel) {
        ItemStack enchantmentBook = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = enchantmentBook.getItemMeta();
        if (meta != null) {
            String enchantmentName = formatItemName(enchantment.getKey().getKey());
            meta.setDisplayName(EnhancedUtils.ChatColors.DARK_PURPLE + enchantmentName);

            List<String> lore = new ArrayList<>();
            int currentLevel = itemToEnchant.getEnchantmentLevel(enchantment);
            lore.add(EnhancedUtils.ChatColors.GRAY + "Level: " + displayLevel);

            if (currentLevel < maxLevel) {
                lore.add(EnhancedUtils.ChatColors.GRAY + "Cost: " + formatCost(enchantmentPrices.getPrice(enchantment, displayLevel)) + " levels");
                lore.add(EnhancedUtils.ChatColors.DARK_PURPLE + "Click to enchant");
            } else {
                lore.clear();
                lore.add(EnhancedUtils.ChatColors.DARK_RED + "Max Level Reached");
            }


            meta.setLore(lore);
            meta.addEnchant(enchantment, displayLevel, true); // Ensure the enchantment is added
            enchantmentBook.setItemMeta(meta);
        }
        return enchantmentBook;
    }

    private String formatCost(int cost) {
        return NumberFormat.getInstance().format(cost);
    }

    private List<EnchantEntry> getValidEnchantments(ItemStack item) {
        Map<Enchantment, EnchantEntry> enchantmentMap = new HashMap<>();

        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment != null && (item.getType() == Material.BOOK || enchantment.canEnchantItem(item))) {
                int maxLevel = enchantment.getMaxLevel();
                int currentLevel = item.getEnchantmentLevel(enchantment);
                int displayLevel = currentLevel < maxLevel ? currentLevel + 1 : maxLevel;
                enchantmentMap.put(enchantment, new EnchantEntry(enchantment, displayLevel));
            }
        }


        return new ArrayList<>(enchantmentMap.values());
    }

    public String formatItemName(String name) {
        StringBuilder formattedName = new StringBuilder();
        for (String word : name.split("_")) {
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return formattedName.toString().trim();
    }

    public ItemStack createGlassPane(Material material) {
        ItemStack glassPane = new ItemStack(material);
        ItemMeta meta = glassPane.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            glassPane.setItemMeta(meta);
        }
        return glassPane;
    }

    public boolean canItemBeEnchanted(ItemStack item) {
        return Arrays.stream(Enchantment.values())
                .filter(Objects::nonNull) // Ensure null enchantments are filtered out
                .anyMatch(enchantment -> enchantment.canEnchantItem(item));
    }


    private boolean isCurse(Enchantment enchantment) {
        return enchantment == Enchantment.BINDING_CURSE || enchantment == Enchantment.VANISHING_CURSE;
    }
}
