package com.aidean.casinoplugin;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;

public class Baccarat {
    private final CasinoPlugin plugin;
    private final Player player;
    private final String betType;
    private final double bet;
    private final Deck deck;
    private final List<Card> playerHand;
    private final List<Card> bankerHand;

    public Baccarat(CasinoPlugin plugin, Player player, String betType, double bet) {
        this.plugin = plugin;
        this.player = player;
        this.betType = betType;
        this.bet = bet;
        this.deck = new Deck();
        this.playerHand = new ArrayList<>();
        this.bankerHand = new ArrayList<>();
    }

    public void start() {
        EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, bet);
        if (!response.transactionSuccess()) {
            player.sendMessage("§cFailed to place bet: " + response.errorMessage);
            return;
        }
        
        player.sendMessage("§e§l=== BACCARAT ===");
        player.sendMessage("§aBet: $" + String.format("%.2f", bet) + " on §e" + betType.toUpperCase());
        
        new BukkitRunnable() {
            @Override
            public void run() {
                dealInitialCards();
            }
        }.runTaskLater(plugin, 20L);
    }

    private void dealInitialCards() {
        playerHand.add(deck.draw());
        bankerHand.add(deck.draw());
        playerHand.add(deck.draw());
        bankerHand.add(deck.draw());

        player.sendMessage("§bPlayer's hand: " + formatHand(playerHand) + " §7(" + getHandValue(playerHand) + ")");
        player.sendMessage("§cBanker's hand: " + formatHand(bankerHand) + " §7(" + getHandValue(bankerHand) + ")");

        int playerValue = getHandValue(playerHand);
        int bankerValue = getHandValue(bankerHand);

        if (playerValue >= 8 || bankerValue >= 8) {
            determineWinner();
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                applyThirdCardRules();
            }
        }.runTaskLater(plugin, 40L);
    }

    private void applyThirdCardRules() {
        int playerValue = getHandValue(playerHand);
        int bankerValue = getHandValue(bankerHand);

        Card playerThirdCard = null;

        if (playerValue <= 5) {
            playerThirdCard = deck.draw();
            playerHand.add(playerThirdCard);
            player.sendMessage("§bPlayer draws: " + playerThirdCard);
            playerValue = getHandValue(playerHand);
        } else {
            player.sendMessage("§bPlayer stands with " + playerValue);
        }

        if (playerThirdCard == null) {
            if (bankerValue <= 5) {
                Card bankerThirdCard = deck.draw();
                bankerHand.add(bankerThirdCard);
                player.sendMessage("§cBanker draws: " + bankerThirdCard);
            } else {
                player.sendMessage("§cBanker stands with " + bankerValue);
            }
        } else {
            int thirdCardValue = playerThirdCard.getValue() % 10;
            boolean bankerDraws = false;

            if (bankerValue <= 2) {
                bankerDraws = true;
            } else if (bankerValue == 3 && thirdCardValue != 8) {
                bankerDraws = true;
            } else if (bankerValue == 4 && thirdCardValue >= 2 && thirdCardValue <= 7) {
                bankerDraws = true;
            } else if (bankerValue == 5 && thirdCardValue >= 4 && thirdCardValue <= 7) {
                bankerDraws = true;
            } else if (bankerValue == 6 && thirdCardValue >= 6 && thirdCardValue <= 7) {
                bankerDraws = true;
            }

            if (bankerDraws) {
                Card bankerThirdCard = deck.draw();
                bankerHand.add(bankerThirdCard);
                player.sendMessage("§cBanker draws: " + bankerThirdCard);
            } else {
                player.sendMessage("§cBanker stands with " + bankerValue);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                determineWinner();
            }
        }.runTaskLater(plugin, 20L);
    }

    private void determineWinner() {
        int playerValue = getHandValue(playerHand);
        int bankerValue = getHandValue(bankerHand);

        player.sendMessage("§e§l=== FINAL RESULTS ===");
        player.sendMessage("§bPlayer's final: " + formatHand(playerHand) + " §7(" + playerValue + ")");
        player.sendMessage("§cBanker's final: " + formatHand(bankerHand) + " §7(" + bankerValue + ")");

        String result;
        double payout = 0;

        if (playerValue == bankerValue) {
            result = "tie";
            if (betType.equals("tie")) {
                payout = bet * 9;
                player.sendMessage("§6§lTIE! You win!");
            } else {
                payout = bet;
                player.sendMessage("§e§lTIE! Bet returned.");
            }
        } else if (playerValue > bankerValue) {
            result = "player";
            if (betType.equals("player")) {
                payout = bet * 2;
                player.sendMessage("§a§lPLAYER WINS! You win!");
            } else {
                player.sendMessage("§c§lPLAYER WINS! You lose.");
            }
        } else {
            result = "banker";
            if (betType.equals("banker")) {
                payout = bet * 1.95;
                player.sendMessage("§a§lBANKER WINS! You win!");
            } else {
                player.sendMessage("§c§lBANKER WINS! You lose.");
            }
        }

        if (payout > 0) {
            EconomyResponse response = plugin.getEconomy().depositPlayer(player, payout);
            if (!response.transactionSuccess()) {
                player.sendMessage("§cWarning: Failed to deposit winnings: " + response.errorMessage);
                plugin.getLogger().severe("Failed to deposit " + payout + " to " + player.getName() + ": " + response.errorMessage);
            } else {
                double profit = payout - bet;
                player.sendMessage("§aWinnings: $" + String.format("%.2f", payout) + 
                                 " §7(Profit: " + (profit >= 0 ? "§a+" : "§c") + 
                                 String.format("%.2f", profit) + "§7)");
            }
        }
        
        player.sendMessage("§7New balance: $" + String.format("%.2f", plugin.getEconomy().getBalance(player)));
    }

    private String formatHand(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(hand.get(i).toString());
        }
        return sb.toString();
    }

    private int getHandValue(List<Card> hand) {
        int value = 0;
        for (Card card : hand) {
            value += card.getValue();
        }
        return value % 10;
    }
}
