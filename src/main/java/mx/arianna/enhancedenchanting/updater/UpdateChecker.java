package mx.arianna.enhancedenchanting.updater;

import mx.arianna.enhancedenchanting.utils.EnhancedUtils;
import mx.arianna.enhancedenchanting.utils.UtilConsole;
import mx.arianna.enhancedenchanting.utils.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker implements Listener {
    private final JavaPlugin plugin;
    private final String updateURL = "https://raw.githubusercontent.com/0x7CD6/EnhancedEnchantments/main/src/main/resources/version.txt";
    private final String repositoryLinkURL = "https://raw.githubusercontent.com/0x7CD6/EnhancedEnchantments/main/src/main/resources/repositoryLink.txt";
    private final String currentVersion;
    private String spigotPage;
    private static final String MESSAGE = "An update is available! Please find it on our Spigot page: ";

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        fetchURLContent(repositoryLinkURL, content -> spigotPage = content);
        checkForUpdates();
    }

    private void fetchURLContent(String urlString, java.util.function.Consumer<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlString).openStream()))) {
                callback.accept(reader.readLine());
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to fetch URL content: " + e.getMessage());
            }
        });
    }

    private void checkForUpdates() {
        fetchURLContent(updateURL, latestVersion -> {
            String status = (latestVersion != null && !currentVersion.equalsIgnoreCase(latestVersion))
                    ? "New Version Available"
                    : "Latest Version";
            String message = "Plugin status: " + EnhancedUtils.formatItem(status) + ".";
            sendConsoleMessage(message);
            if ("New Version Available".equals(status)) notifyUpdateAvailable();
        });
    }

    private void notifyUpdateAvailable() {
        String updateMessage = MESSAGE + EnhancedUtils.formatItem(spigotPage) + ".";
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.hasPermission("enhancedenchanting.update"))
                    .forEach(player -> new UtilMessage(player).format(updateMessage));
            sendConsoleMessage(updateMessage);
        });
    }

    private void sendConsoleMessage(String message) {
        new UtilConsole(Bukkit.getConsoleSender()).format(message);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("enhancedenchanting.update")) {
            new UtilMessage(event.getPlayer()).format(MESSAGE + EnhancedUtils.formatItem(spigotPage) + ".");
        }
    }
}
