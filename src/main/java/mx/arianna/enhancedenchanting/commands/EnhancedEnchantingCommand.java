package mx.arianna.enhancedenchanting.commands;

import mx.arianna.enhancedenchanting.configuration.EnchantmentPrices;
import mx.arianna.enhancedenchanting.utils.EnhancedUtils;
import mx.arianna.enhancedenchanting.utils.UtilConsole;
import mx.arianna.enhancedenchanting.utils.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnhancedEnchantingCommand implements CommandExecutor, TabCompleter {

    private final EnchantmentPrices enchantmentPrices;
    private final JavaPlugin plugin;

    public EnhancedEnchantingCommand(JavaPlugin plugin, EnchantmentPrices enchantmentPrices) {
        this.plugin = plugin;
        this.enchantmentPrices = enchantmentPrices;
        PluginCommand enhancedEnchantingCommand = plugin.getCommand("ee");
        if (enhancedEnchantingCommand != null) {
            enhancedEnchantingCommand.setExecutor(this);
            enhancedEnchantingCommand.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            new UtilConsole(Bukkit.getConsoleSender()).format("This is a player-only command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            displayUsageMessage(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "setprice": return handleSetPriceCommand(player, args);
            case "list": return handleListCommand(player);
            case "reload": return handleReloadCommand(player);
            case "info": return handleInfoCommand(player, args);
            default: displayUsageMessage(player);
            return true;
        }
    }
    private void displayUsageMessage(Player player) {
        StringBuilder usageMessage = new StringBuilder("/ee <");
        boolean hasPermission = false;

        if (player.hasPermission("enhancedenchantments.price")) {
            usageMessage.append("setprice|");
            hasPermission = true;
        }
        if (player.hasPermission("enhancedenchantments.list")) {
            usageMessage.append("list|");
            hasPermission = true;
        }
        if (player.hasPermission("enhancedenchantments.reload")) {
            usageMessage.append("reload|");
            hasPermission = true;
        }
        if (player.hasPermission("enhancedenchantments.info")) {
            usageMessage.append("info|");
            hasPermission = true;
        }

        if (hasPermission) {
            // Remove the last '|'
            usageMessage.setLength(usageMessage.length() - 1);
            usageMessage.append(">");
        } else {
            usageMessage = new StringBuilder("You do not have permission to use any EnhancedEnchanting commands.");
        }

        new UtilMessage(player).format("Usage: " + EnhancedUtils.formatItem(usageMessage.toString()));
    }

    private boolean handleSetPriceCommand(Player player, String[] args) {
        if (!player.hasPermission("enhancedenchantments.price")) {
            new UtilMessage(player).format("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 4) {
            new UtilMessage(player).format("Usage: " + EnhancedUtils.formatItem("/ee setprice <enchantment> <level> <price>"));
            return false;
        }

        String enchantmentName = args[1].toUpperCase();
        int level;
        int cost;

        try {
            level = Integer.parseInt(args[2]);
            cost = Integer.parseInt(args[3]);
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
        new UtilMessage(player).format("Set the cost of " + EnhancedUtils.formatItem(enchantmentName) + " level " + EnhancedUtils.formatItem(level) + " to " + EnhancedUtils.formatItem(cost) + " Experience Levels..");
        return true;
    }

    private boolean handleListCommand(Player player) {
        if (!player.hasPermission("enhancedenchantments.list")) {
            new UtilMessage(player).format("You do not have permission to use this command.");
            return true;
        }

        StringBuilder message = new StringBuilder(EnhancedUtils.ChatColors.GOLD + "Available Enchantments and Prices:\n");
        enchantmentPrices.getAllPrices().forEach((enchantment, prices) -> {
            message.append(EnhancedUtils.ChatColors.LIGHT_PURPLE)
                    .append(enchantment.getKey().getKey())
                    .append(": ")
                    .append(EnhancedUtils.ChatColors.DARK_AQUA);
            prices.forEach((level, price) -> {
                message.append(level)
                        .append(": ")
                        .append(EnhancedUtils.ChatColors.DARK_AQUA)
                        .append(price)
                        .append(" XP")
                        .append(EnhancedUtils.ChatColors.GREEN)
                        .append(" | ");
            });
            // Remove the trailing " | " and add a newline
            message.setLength(message.length() - 3);
            message.append("\n");
        });

        new UtilMessage(player).format(message.toString());
        return true;
    }

    private boolean handleReloadCommand(Player player) {
        if (!player.hasPermission("enhancedenchantments.reload")) {
            new UtilMessage(player).format("You do not have permission to use this command.");
            return true;
        }

        plugin.reloadConfig();
        new UtilMessage(player).format("Plugin configuration reloaded.");
        return true;
    }

    private boolean handleInfoCommand(Player player, String[] args) {
        if (!player.hasPermission("enhancedenchantments.info")) {
            new UtilMessage(player).format("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            new UtilMessage(player).format("Usage: " + EnhancedUtils.formatItem("/ee info <enchantment>"));
            return true;
        }

        String enchantmentName = args[1].toUpperCase();
        Enchantment enchantment = EnhancedUtils.getEnchantmentByName(enchantmentName);
        if (enchantment == null) {
            new UtilMessage(player).format("Invalid enchantment name.");
            return true;
        }

        new UtilMessage(player).format("Information for Enchantment: " + EnhancedUtils.formatItem(enchantmentName));
        for (int level = 1; level <= enchantment.getMaxLevel(); level++) {
            int price = enchantmentPrices.getPrice(enchantment, level);
            new UtilMessage(player).format("Level " + EnhancedUtils.formatItem(level) + ": " + EnhancedUtils.formatItem(price) + " Experience Levels.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            List<String> subcommands = new ArrayList<>();
            if (player.hasPermission("enhancedenchantments.price")) subcommands.add("setprice");
            if (player.hasPermission("enhancedenchantments.list")) subcommands.add("list");
            if (player.hasPermission("enhancedenchantments.reload")) subcommands.add("reload");
            if (player.hasPermission("enhancedenchantments.info")) subcommands.add("info");

            return subcommands.stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("setprice") || args[0].equalsIgnoreCase("info"))) {
            if ((args[0].equalsIgnoreCase("setprice") && player.hasPermission("enhancedenchantments.price")) ||
                    (args[0].equalsIgnoreCase("info") && player.hasPermission("enhancedenchantments.info"))) {
                return Arrays.stream(Enchantment.values())
                        .map(enchantment -> enchantment.getKey().getKey())
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("setprice") && player.hasPermission("enhancedenchantments.price")) {
            Enchantment enchantment = EnhancedUtils.getEnchantmentByName(args[1].toUpperCase());
            if (enchantment != null) {
                List<String> levels = new ArrayList<>();
                for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                    levels.add(String.valueOf(i));
                }
                return levels;
            }
        }
        return new ArrayList<>();
    }

}
