package mx.arianna.enhancedenchanting.utils;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class UtilMessage {

    private final Player player;

    public void format(String message) {
        player.sendMessage(EnhancedUtils.ChatColors.PRIMARY + "[Enhanced Enchantments] " + EnhancedUtils.ChatColors.SECONDARY + message);
    }
}
