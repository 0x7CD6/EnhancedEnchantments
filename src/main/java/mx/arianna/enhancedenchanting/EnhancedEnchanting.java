package mx.arianna.enhancedenchanting;

import mx.arianna.enhancedenchanting.commands.SetEnchantmentCostCommand;
import mx.arianna.enhancedenchanting.commands.SetEnchantmentCostTabCompleter;
import mx.arianna.enhancedenchanting.configuration.EnchantmentPrices;
import mx.arianna.enhancedenchanting.gui.GUIHandler;
import mx.arianna.enhancedenchanting.updater.UpdateChecker;
import mx.arianna.enhancedenchanting.utils.EnhancedUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public final class EnhancedEnchanting extends JavaPlugin {
    private final Map<UUID, Integer> playerPages = new HashMap<>();
    private final Set<UUID> forceClose = new HashSet<>();

    @Override
    public void onEnable() {
        new UpdateChecker(this);
        EnchantmentPrices enchantmentPrices = new EnchantmentPrices(this);
        new SetEnchantmentCostCommand(this, enchantmentPrices);
        getServer().getPluginManager().registerEvents(new GUIHandler(playerPages, forceClose, enchantmentPrices), this);
        PluginCommand setEnchantCostCommand = this.getCommand("setenchantcost");
        if (setEnchantCostCommand != null) {
            setEnchantCostCommand.setExecutor(new SetEnchantmentCostCommand(this, enchantmentPrices));
            setEnchantCostCommand.setTabCompleter(new SetEnchantmentCostTabCompleter());
        }
    }

    @Override
    public void onDisable() {

    }
}