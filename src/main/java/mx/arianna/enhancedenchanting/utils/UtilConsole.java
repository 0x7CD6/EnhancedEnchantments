package mx.arianna.enhancedenchanting.utils;

import lombok.AllArgsConstructor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class UtilConsole {

    private final ConsoleCommandSender sender;

    public void format(String message) {
        sender.sendMessage(EnhancedUtils.ChatColors.PRIMARY + "[Enhanced Enchantments] " + EnhancedUtils.ChatColors.SECONDARY + message);
    }
}
