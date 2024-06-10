![Enhanced Enchantments](https://i.imgur.com/jGbYQ3f.png)

## Overview
Enhanced Enchantments is a powerful Minecraft plugin designed to elevate your server's enchanting experience. With customizable enchantment prices, intuitive commands, and advanced permissions management, this plugin is a must-have for any server admin looking to provide a richer gameplay experience.

## Features
- **Custom Enchantment Prices**: Easily set and adjust prices for different enchantments and levels.
- **Enchantment Listing**: List all available enchantments along with their prices using a simple command.
- **Update Notifications**: Receive notifications for new plugin updates to keep your server up-to-date.
- **Player-Friendly Commands**: Intuitive commands for setting prices, listing enchantments, reloading the configuration, and getting enchantment info.
- **Permissions Management**: Fine-grained permissions to control access to various commands.

## Commands
- `/ee setprice <enchantment> <level> <price>`: Set the price for a specific enchantment level.
- `/ee list`: List all available enchantments and their prices in a clean and readable format.
- `/ee reload`: Reload the plugin configuration without restarting the server.
- `/ee info <enchantment>`: Display detailed information about a specific enchantment.

## Permissions
- `enhancedenchantments.use`: General permission to use the /ee command.
- `enhancedenchantments.price`: Permission to set enchantment prices.
- `enhancedenchantments.list`: Permission to list enchantments and prices.
- `enhancedenchantments.reload`: Permission to reload the plugin configuration.
- `enhancedenchantments.info`: Permission to view detailed information about enchantments.
- `enhancedenchantments.update`: Permission to view if the plugin needs an update.

## Installation
1. Download the latest version of the Enhanced Enchantments plugin.
2. Place the `EnhancedEnchantments.jar` file into your server's `plugins` directory.
3. Start or restart your server to load the plugin.
4. Configure the plugin as needed in the `config.yml` file located in the `plugins/EnhancedEnchantments` directory.

## Configuration Example
```yaml
enchantments:
  sharpness:
    price:
      1: 10
      2: 20
      3: 30
      4: 40
      5: 50
  efficiency:
    price:
      1: 5
      2: 15
      3: 25
      4: 35
      5: 45
```

## Support
For support or inquiries, please open an issue on the [GitHub repository](https://github.com/0x7CD6/EnhancedEnchantments/issues) or contact me directly through GitHub.

## License
This plugin is licensed under the Apache License 2.0. See the [LICENSE](https://github.com/0x7CD6/EnhancedEnchantments/blob/main/LICENSE) file for details.

Thank you for using Enhanced Enchantments! I hope this plugin enhances your Minecraft server's enchanting experience. Happy enchanting!

**Note**: This plugin is not affiliated with or endorsed by Mojang AB. Minecraft is a trademark of Mojang AB.
