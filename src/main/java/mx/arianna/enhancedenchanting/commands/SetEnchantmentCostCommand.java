package mx.arianna.enhancedenchanting.commands;

import mx.arianna.enhancedenchanting.configuration.EnchantmentPrices;
import mx.arianna.enhancedenchanting.enchants.CustomEnchantment;
import mx.arianna.enhancedenchanting.utils.EnhancedUtils;
import mx.arianna.enhancedenchanting.utils.UtilMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetEnchantmentCostCommand implements CommandExecutor {

    private final EnchantmentPrices enchantmentPrices;
    private final JavaPlugin plugin;

    public SetEnchantmentCostCommand(JavaPlugin plugin, EnchantmentPrices enchantmentPrices) {
        this.plugin = plugin;
        this.enchantmentPrices = enchantmentPrices;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(EnhancedUtils.ChatColors.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("enhancedenchantments.price")) {
            new UtilMessage(player).format("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 3) {
            new UtilMessage(player).format("Usage: " + EnhancedUtils.formatItem("/setenchantcost <enchantment> <level> <cost>"));
            return false;
        }

        String enchantmentName = args[0].toUpperCase();
        int level;
        int cost;

        try {
            level = Integer.parseInt(args[1]);
            cost = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            new UtilMessage(player).format("Level and cost must be valid numbers.");
            return false;
        }

        Enchantment enchantment = EnhancedUtils.getEnchantmentByName(enchantmentName);
        if (enchantment == null) {
            new UtilMessage(player).format("Invalid enchantment name.");
            return false;
        }

        if (level < 1 || level > enchantment.getMaxLevel()) {
            new UtilMessage(player).format("Invalid enchantment level.");
            return false;
        }

        // Check if the cost is lower than the previous level if the level is higher or equal to 2
        if (level >= 2) {
            int previousLevelCost = enchantmentPrices.getPrice(enchantment, level - 1);
            if (cost <= previousLevelCost) {
                new UtilMessage(player).format("The cost of level " + EnhancedUtils.formatItem(level) + " cannot be lower or the same as the cost of level " + EnhancedUtils.formatItem(level - 1) + ".");
                return false;
            }
        }

        enchantmentPrices.setPrice(enchantment, level, cost);
        new UtilMessage(player).format("Set the cost of " + EnhancedUtils.formatItem(enchantmentName) + " level " + EnhancedUtils.formatItem(level) + " to " + EnhancedUtils.formatItem(cost) + " levels.");
        return true;
    }
}
