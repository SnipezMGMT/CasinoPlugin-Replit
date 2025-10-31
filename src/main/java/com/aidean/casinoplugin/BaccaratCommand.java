package com.aidean.casinoplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaccaratCommand implements CommandExecutor {
    private final CasinoPlugin plugin;

    public BaccaratCommand(CasinoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can play baccarat!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§e=== Baccarat Commands ===");
            player.sendMessage("§a/baccarat play <player|banker|tie> <bet> §7- Start a baccarat game");
            player.sendMessage("§a/baccarat rules §7- View baccarat rules");
            return true;
        }

        if (args[0].equalsIgnoreCase("rules")) {
            player.sendMessage("§e=== Baccarat Rules ===");
            player.sendMessage("§7Bet on Player, Banker, or Tie.");
            player.sendMessage("§7The hand closest to 9 wins.");
            player.sendMessage("§7Player pays 1:1, Banker pays 0.95:1, Tie pays 8:1");
            return true;
        }

        if (args[0].equalsIgnoreCase("play")) {
            if (args.length < 3) {
                player.sendMessage("§cUsage: /baccarat play <player|banker|tie> <bet>");
                return true;
            }

            String betType = args[1].toLowerCase();
            if (!betType.equals("player") && !betType.equals("banker") && !betType.equals("tie")) {
                player.sendMessage("§cInvalid bet type! Use: player, banker, or tie");
                return true;
            }

            double bet;
            try {
                bet = Double.parseDouble(args[2]);
                if (bet <= 0) {
                    player.sendMessage("§cBet must be greater than 0!");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid bet amount!");
                return true;
            }

            if (plugin.getEconomy() == null) {
                player.sendMessage("§cEconomy system not available!");
                return true;
            }

            if (plugin.getEconomy().getBalance(player) < bet) {
                player.sendMessage("§cYou don't have enough money! Balance: $" + 
                    String.format("%.2f", plugin.getEconomy().getBalance(player)));
                return true;
            }

            Baccarat game = new Baccarat(plugin, player, betType, bet);
            game.start();
            return true;
        }

        return false;
    }
}
