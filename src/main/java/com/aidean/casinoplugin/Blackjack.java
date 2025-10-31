package com.aidean.casinoplugin;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;

public class Blackjack {
    private final CasinoPlugin plugin;
    private final Player player;
    private double bet;
    private final Deck deck;
    private final List<Card> playerHand;
    private final List<Card> dealerHand;
    private boolean hideDealer;
    private double insuranceBet;
    private double perfectPairsBet;

    public Blackjack(CasinoPlugin plugin, Player player, double bet) {
        this.plugin = plugin;
        this.player = player;
        this.bet = bet;
        this.deck = new Deck();
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.hideDealer = true;
        this.insuranceBet = 0;
        this.perfectPairsBet = 0;
    }

    public void start() {
        EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, bet);
        if (!response.transactionSuccess()) {
            player.sendMessage("§cFailed to place bet: " + response.errorMessage);
            return;
        }
        
        player.sendMessage("§e§l=== BLACKJACK ===");
        player.sendMessage("§aBet placed: $" + String.format("%.2f", bet));
        
        playerHand.add(deck.draw());
        dealerHand.add(deck.draw());
        playerHand.add(deck.draw());
        dealerHand.add(deck.draw());

        displayHands(true);

        int playerValue = getHandValue(playerHand);
        int dealerValue = getHandValue(dealerHand);

        if (playerValue == 21) {
            if (dealerValue == 21) {
                player.sendMessage("§e§lBoth have BLACKJACK! Push!");
                displayHands(false);
                win(bet);
                endGame();
                return;
            }
            player.sendMessage("§6§lBLACKJACK! You win!");
            displayHands(false);
            win(bet * 2.5);
            endGame();
            return;
        }

        GUIManager.openBlackjackGUI(player, this);
    }

    public void hit() {
        playerHand.add(deck.draw());
        displayHands(true);
        GUIManager.updateBlackjackGUI(player, this);

        int playerValue = getHandValue(playerHand);
        if (playerValue > 21) {
            player.sendMessage("§c§lBUST! You lose!");
            player.closeInventory();
            endGame();
        } else if (playerValue == 21) {
            stand();
        }
    }
    
    public void doubleDown() {
        if (!canDoubleDown()) {
            player.sendMessage("§cCannot double down!");
            return;
        }
        
        EconomyResponse response = plugin.getEconomy().withdrawPlayer(player, bet);
        if (!response.transactionSuccess()) {
            player.sendMessage("§cFailed to double down: " + response.errorMessage);
            return;
        }
        
        bet *= 2;
        player.sendMessage("§6Doubled down! New bet: $" + String.format("%.2f", bet));
        
        playerHand.add(deck.draw());
        displayHands(true);
        GUIManager.updateBlackjackGUI(player, this);
        player.sendMessage("§7Drew: " + playerHand.get(playerHand.size() - 1));
        
        new BukkitRunnable() {
            @Override
            public void run() {
                stand();
            }
        }.runTaskLater(plugin, 20L);
    }

    public void stand() {
        player.closeInventory();
        hideDealer = false;
        player.sendMessage("§eYou stand with " + getHandValue(playerHand));
        
        new BukkitRunnable() {
            @Override
            public void run() {
                dealerPlay();
            }
        }.runTaskLater(plugin, 20L);
    }

    private void dealerPlay() {
        hideDealer = false;
        displayHands(false);
        
        while (getHandValue(dealerHand) < 17) {
            dealerHand.add(deck.draw());
            player.sendMessage("§7Dealer draws: " + dealerHand.get(dealerHand.size() - 1));
        }

        int playerValue = getHandValue(playerHand);
        int dealerValue = getHandValue(dealerHand);

        player.sendMessage("§eDealer's final hand: " + dealerValue);
        
        if (dealerValue > 21) {
            player.sendMessage("§a§lDealer BUSTS! You win!");
            win(bet * 2);
        } else if (playerValue > dealerValue) {
            player.sendMessage("§a§lYou win!");
            win(bet * 2);
        } else if (playerValue == dealerValue) {
            player.sendMessage("§e§lPUSH! Bet returned.");
            win(bet);
        } else {
            player.sendMessage("§c§lDealer wins!");
        }
        
        endGame();
    }

    private void win(double amount) {
        EconomyResponse response = plugin.getEconomy().depositPlayer(player, amount);
        if (!response.transactionSuccess()) {
            player.sendMessage("§cWarning: Failed to deposit winnings: " + response.errorMessage);
            plugin.getLogger().severe("Failed to deposit " + amount + " to " + player.getName() + ": " + response.errorMessage);
            return;
        }
        
        double profit = amount - bet;
        player.sendMessage("§aWinnings: $" + String.format("%.2f", amount) + 
                         " §7(Profit: " + (profit >= 0 ? "§a+" : "§c") + 
                         String.format("%.2f", profit) + "§7)");
        player.sendMessage("§7New balance: $" + String.format("%.2f", plugin.getEconomy().getBalance(player)));
    }

    private void endGame() {
        GUIManager.removeActiveGame(player);
    }

    private void displayHands(boolean hideDealer) {
        player.sendMessage("§e§lYour hand: §f" + formatHand(playerHand) + " §7(" + getHandValue(playerHand) + ")");
        
        if (hideDealer) {
            player.sendMessage("§c§lDealer's hand: §f" + dealerHand.get(0) + " §8[Hidden]");
        } else {
            player.sendMessage("§c§lDealer's hand: §f" + formatHand(dealerHand) + " §7(" + getHandValue(dealerHand) + ")");
        }
    }

    private String formatHand(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(hand.get(i).toString());
        }
        return sb.toString();
    }

    public Player getPlayer() {
        return player;
    }
    
    public List<Card> getPlayerHand() {
        return playerHand;
    }
    
    public List<Card> getDealerHand() {
        return dealerHand;
    }
    
    public double getBet() {
        return bet;
    }
    
    public boolean isHidingDealer() {
        return hideDealer;
    }
    
    public int getHandValue(List<Card> hand) {
        int value = 0;
        int aces = 0;

        for (Card card : hand) {
            value += card.getValue();
            if (card.getRank() == Card.Rank.ACE) {
                aces++;
            }
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }
    
    public int getCardValue(Card card) {
        return card.getValue();
    }
    
    public boolean canDoubleDown() {
        return playerHand.size() == 2 && plugin.getEconomy().getBalance(player) >= bet;
    }
}
