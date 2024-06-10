package mx.arianna.enhancedenchanting.updater;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private JavaPlugin plugin;
    private String updateURL;
    private String currentVersion;

    public UpdateChecker(JavaPlugin plugin, String updateURL) {
        this.plugin = plugin;
        this.updateURL = updateURL;
        this.currentVersion = plugin.getDescription().getVersion();
    }

    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(updateURL).openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String latestVersion = reader.readLine();
                reader.close();

                if (latestVersion != null && !currentVersion.equalsIgnoreCase(latestVersion)) {
                    notifyUpdateAvailable(latestVersion);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to check for updates: " + e.getMessage());
            }
        });
    }

    private void notifyUpdateAvailable(String latestVersion) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.hasPermission("enhancedenchanting.update"))
                    .forEach(player -> player.sendMessage("§aAn update is available! Latest version: " + latestVersion));
        });
    }
}
