package mx.arianna.enhancedenchanting.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    private FileConfiguration config;
    private File configFile;
    private JavaPlugin plugin;

    public ConfigLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            File parentFile = configFile.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                boolean created = parentFile.mkdirs();
                if (!created) {
                    plugin.getLogger().severe("Could not create config directory!");
                    return;
                }
            }
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}