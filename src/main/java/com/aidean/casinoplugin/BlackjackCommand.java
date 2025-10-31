package com.aidean.casinoplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlackjackCommand implements CommandExecutor {
    private final CasinoPlugin plugin;

    public BlackjackCommand(CasinoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can play blackjack!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§e=== Blackjack Commands ===");
            player.sendMessage("§a/blackjack play <bet> §7- Start a blackjack game");
            player.sendMessage("§a/blackjack rules §7- View blackjack rules");
            return true;
        }

        if (args[0].equalsIgnoreCase("rules")) {
            player.sendMessage("§e=== Blackjack Rules ===");
            player.sendMessage("§7Get as close to 21 as possible without going over.");
            player.sendMessage("§7Face cards are worth 10, Aces are worth 11 or 1.");
            player.sendMessage("§7Beat the dealer to win!");
            return true;
        }

        if (args[0].equalsIgnoreCase("play")) {
            if (args.length < 2) {
                player.sendMessage("§cUsage: /blackjack play <bet>");
                return true;
            }

            double bet;
            try {
                bet = Double.parseDouble(args[1]);
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

            Blackjack game = new Blackjack(plugin, player, bet);
            game.start();
            return true;
        }

        return false;
    }
}
