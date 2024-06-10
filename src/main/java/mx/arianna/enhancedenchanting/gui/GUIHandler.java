package mx.arianna.enhancedenchanting.gui;

import mx.arianna.enhancedenchanting.configuration.EnchantmentPrices;
import mx.arianna.enhancedenchanting.utils.EnhancedUtils;
import mx.arianna.enhancedenchanting.utils.UtilMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GUIHandler implements Listener {
    private final Map<UUID, Integer> playerPages;
    private final Set<UUID> forceClose;
    private final EnchantmentPrices enchantmentPrices;
    private final GUIUtils guiUtils;

    public GUIHandler(Map<UUID, Integer> playerPages, Set<UUID> forceClose, EnchantmentPrices enchantmentPrices) {
        this.guiUtils = new GUIUtils(enchantmentPrices);
        this.playerPages = playerPages;
        this.forceClose = forceClose;
        this.enchantmentPrices = enchantmentPrices;
    }

    @EventHandler
    public void onEnchantInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null
                && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            event.setCancelled(true);
            guiUtils.openInitialGUI(player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        String title = event.getView().getTitle();

        if (forceClose.remove(player.getUniqueId())) return;

        if (title.equals(EnhancedUtils.ChatColors.DARK_PURPLE + "Insert Item") || title.equals(EnhancedUtils.ChatColors.DARK_PURPLE + "Enchantment Options")) {
            playerPages.remove(player.getUniqueId());
            handleInventoryClose(player, event.getInventory());
        }
    }

    @EventHandler
    public void onEnchantmentSelect(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        String title = event.getView().getTitle();
        if (title.equals(EnhancedUtils.ChatColors.DARK_PURPLE + "Insert Item")) {
            event.setCancelled(true);
            handleInsertItemClick(event, player, clickedItem);
        } else if (title.equals(EnhancedUtils.ChatColors.DARK_PURPLE + "Enchantment Options")) {
            event.setCancelled(true);
            handleEnchantmentOptionClick(event, player, clickedItem);
        }
    }

    private void handleInventoryClose(Player player, Inventory inventory) {
        ItemStack itemInMiddleSlot = inventory.getItem(4);
        if (itemInMiddleSlot != null && itemInMiddleSlot.getType() != Material.AIR) {
            HashMap<Integer, ItemStack> notFitted = player.getInventory().addItem(itemInMiddleSlot);
            notFitted.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
            inventory.setItem(4, null);
        }
    }

    private void handleInsertItemClick(InventoryClickEvent event, Player player, ItemStack clickedItem) {
        if (clickedItem.getType() != Material.AIR) {
            if (event.getRawSlot() == 4 || guiUtils.canItemBeEnchanted(clickedItem)) {
                player.closeInventory();
                guiUtils.openEnchantmentGUI(player, clickedItem.clone());
                event.setCurrentItem(null);
            }
        }
    }

    private void handleEnchantmentOptionClick(InventoryClickEvent event, Player player, ItemStack clickedItem) {
        if (clickedItem.getType() == Material.ENCHANTED_BOOK) {
            handleEnchantmentBookClick(player, clickedItem, event.getInventory(), forceClose);
        } else if (event.getRawSlot() == 4 && clickedItem.getType() != Material.AIR) {
            player.getInventory().addItem(clickedItem);
            event.setCurrentItem(null);
            player.closeInventory();
            guiUtils.openInitialGUI(player);
        }
    }

    public void handleEnchantmentBookClick(Player player, ItemStack clickedItem, Inventory inventory, Set<UUID> forceClose) {
        try {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta == null || meta.getEnchants().isEmpty()) {
                new UtilMessage(player).format("No enchantments found on this book.");
                return;
            }

            Enchantment selectedEnchant = meta.getEnchants().keySet().stream().findFirst().orElse(null);
            if (selectedEnchant == null) {
                new UtilMessage(player).format("No enchantments found on this item.");
                return;
            }

            ItemStack centralItem = inventory.getItem(4);
            if (centralItem == null) {
                new UtilMessage(player).format("No item found in the center slot.");
                return;
            }

            int currentLevel = centralItem.getEnchantmentLevel(selectedEnchant);
            int nextLevel = currentLevel + 1;

            if (currentLevel >= selectedEnchant.getMaxLevel()) return;

            int requiredLevels = enchantmentPrices.getPrice(selectedEnchant, nextLevel);
            if (player.getLevel() < requiredLevels) return;

            player.setLevel(player.getLevel() - requiredLevels);
            centralItem.addUnsafeEnchantment(selectedEnchant, nextLevel);
            String enchantmentName = guiUtils.formatItemName(selectedEnchant.getKey().getKey());
//            new UtilMessage(player).format("Successfully enchanted the item with " + EnhancedUtils.formatItem(enchantmentName) + " at level " + EnhancedUtils.formatItem(nextLevel) + " for the price of " + EnhancedUtils.formatItem(String.valueOf(requiredLevels)) + " levels.");
            forceClose.add(player.getUniqueId()); // Prevent onInventoryClose actions
//            player.closeInventory(); // Close the inventory to prevent duplication
            guiUtils.openEnchantmentGUI(player, centralItem); // Reopen the GUI with the updated item
        } catch (Exception e) {
            e.printStackTrace();
            new UtilMessage(player).format("An error occurred while processing the enchantment. Please try again.");
        }
    }
}
