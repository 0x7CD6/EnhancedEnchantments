package mx.arianna.enhancedenchanting.commands;

import mx.arianna.enhancedenchanting.utils.EnhancedUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetEnchantmentCostTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Suggest enchantment names
            return Arrays.stream(Enchantment.values())
                    .map(enchantment -> enchantment.getKey().getKey())
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            // Suggest levels based on the selected enchantment
            Enchantment enchantment = EnhancedUtils.getEnchantmentByName(args[0].toUpperCase());
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
