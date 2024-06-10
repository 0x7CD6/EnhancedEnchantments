package mx.arianna.enhancedenchanting;

import mx.arianna.enhancedenchanting.commands.EnhancedEnchantingCommand;
import mx.arianna.enhancedenchanting.configuration.EnchantmentPrices;
import mx.arianna.enhancedenchanting.gui.GUIHandler;
import mx.arianna.enhancedenchanting.updater.UpdateChecker;
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
        new EnhancedEnchantingCommand(this, enchantmentPrices);
        getServer().getPluginManager().registerEvents(new GUIHandler(playerPages, forceClose, enchantmentPrices), this);
    }

    @Override
    public void onDisable() {

    }
}