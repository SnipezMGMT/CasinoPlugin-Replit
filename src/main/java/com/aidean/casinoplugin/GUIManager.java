package com.aidean.casinoplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIManager implements Listener {
    private static final Map<Player, Blackjack> activeBlackjackGames = new HashMap<>();
    private static final Map<Player, Baccarat> activeBaccaratGames = new HashMap<>();

    public static void openBlackjackGUI(Player player, Blackjack game) {
        activeBlackjackGames.put(player, game);
        
        Inventory gui = Bukkit.createInventory(null, 54, "§0§lBlackjack Table");

        ItemStack divider = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta dividerMeta = divider.getItemMeta();
        dividerMeta.setDisplayName(" ");
        divider.setItemMeta(dividerMeta);

        for (int i = 0; i < 9; i++) {
            gui.setItem(i + 18, divider);
        }

        updateBlackjackDisplay(gui, game);

        ItemStack hit = new ItemStack(Material.LIME_WOOL);
        ItemMeta hitMeta = hit.getItemMeta();
        hitMeta.setDisplayName("§a§lHIT");
        hitMeta.setLore(Arrays.asList("§7Draw another card"));
        hit.setItemMeta(hitMeta);

        ItemStack stand = new ItemStack(Material.RED_WOOL);
        ItemMeta standMeta = stand.getItemMeta();
        standMeta.setDisplayName("§c§lSTAND");
        standMeta.setLore(Arrays.asList("§7Keep your current hand"));
        stand.setItemMeta(standMeta);

        ItemStack doubleDown = new ItemStack(Material.GOLD_INGOT);
        ItemMeta doubleMeta = doubleDown.getItemMeta();
        doubleMeta.setDisplayName("§6§lDOUBLE DOWN");
        doubleMeta.setLore(Arrays.asList(
            "§7Double your bet",
            "§7Get one more card",
            "§7Then auto-stand"
        ));
        doubleDown.setItemMeta(doubleMeta);

        gui.setItem(47, hit);
        gui.setItem(49, stand);
        if (game.canDoubleDown()) {
            gui.setItem(51, doubleDown);
        }

        player.openInventory(gui);
    }

    private static void updateBlackjackDisplay(Inventory gui, Blackjack game) {
        List<Card> dealerHand = game.getDealerHand();
        List<Card> playerHand = game.getPlayerHand();
        boolean hideDealer = game.isHidingDealer();

        int dealerSlot = 2;
        for (int i = 0; i < dealerHand.size(); i++) {
            if (i == 1 && hideDealer) {
                gui.setItem(dealerSlot++, CardItem.createHiddenCard());
            } else {
                gui.setItem(dealerSlot++, CardItem.createCard(dealerHand.get(i)));
            }
        }

        ItemStack dealerInfo = new ItemStack(Material.PAPER);
        ItemMeta dealerMeta = dealerInfo.getItemMeta();
        dealerMeta.setDisplayName("§c§lDealer's Hand");
        int dealerValue = hideDealer ? game.getCardValue(dealerHand.get(0)) : game.getHandValue(dealerHand);
        dealerMeta.setLore(Arrays.asList("§7Value: §f" + (hideDealer ? dealerValue + " + ?" : dealerValue)));
        dealerInfo.setItemMeta(dealerMeta);
        gui.setItem(0, dealerInfo);

        int playerSlot = 29;
        for (Card card : playerHand) {
            gui.setItem(playerSlot++, CardItem.createCard(card));
        }

        ItemStack playerInfo = new ItemStack(Material.EMERALD);
        ItemMeta playerMeta = playerInfo.getItemMeta();
        playerMeta.setDisplayName("§a§lYour Hand");
        playerMeta.setLore(Arrays.asList(
            "§7Value: §f" + game.getHandValue(playerHand),
            "§7Bet: §6$" + String.format("%.2f", game.getBet())
        ));
        playerInfo.setItemMeta(playerMeta);
        gui.setItem(27, playerInfo);
    }

    public static void openBaccaratGUI(Player player, Baccarat game) {
        activeBaccaratGames.put(player, game);
        
        Inventory gui = Bukkit.createInventory(null, 54, "§0§lBaccarat Table");

        ItemStack divider = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta dividerMeta = divider.getItemMeta();
        dividerMeta.setDisplayName(" ");
        divider.setItemMeta(dividerMeta);

        for (int i = 0; i < 9; i++) {
            gui.setItem(i + 22, divider);
        }

        updateBaccaratDisplay(gui, game);

        player.openInventory(gui);
    }

    private static void updateBaccaratDisplay(Inventory gui, Baccarat game) {
        List<Card> playerHand = game.getPlayerHand();
        List<Card> bankerHand = game.getBankerHand();

        int playerSlot = 2;
        for (Card card : playerHand) {
            gui.setItem(playerSlot++, CardItem.createCard(card));
        }

        ItemStack playerInfo = new ItemStack(Material.BLUE_CONCRETE);
        ItemMeta playerMeta = playerInfo.getItemMeta();
        playerMeta.setDisplayName("§b§lPlayer's Hand");
        playerMeta.setLore(Arrays.asList("§7Value: §f" + game.getHandValue(playerHand)));
        playerInfo.setItemMeta(playerMeta);
        gui.setItem(0, playerInfo);

        int bankerSlot = 11;
        for (Card card : bankerHand) {
            gui.setItem(bankerSlot++, CardItem.createCard(card));
        }

        ItemStack bankerInfo = new ItemStack(Material.RED_CONCRETE);
        ItemMeta bankerMeta = bankerInfo.getItemMeta();
        bankerMeta.setDisplayName("§c§lBanker's Hand");
        bankerMeta.setLore(Arrays.asList("§7Value: §f" + game.getHandValue(bankerHand)));
        bankerInfo.setItemMeta(bankerMeta);
        gui.setItem(9, bankerInfo);

        ItemStack betInfo = new ItemStack(Material.GOLD_INGOT);
        ItemMeta betMeta = betInfo.getItemMeta();
        betMeta.setDisplayName("§e§lYour Bet");
        betMeta.setLore(Arrays.asList(
            "§7Betting on: §f" + game.getBetType().toUpperCase(),
            "§7Amount: §6$" + String.format("%.2f", game.getBet())
        ));
        betInfo.setItemMeta(betMeta);
        gui.setItem(49, betInfo);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.equals("§0§lBlackjack Table")) {
            event.setCancelled(true);
            
            Blackjack game = activeBlackjackGames.get(player);
            if (game == null) {
                player.closeInventory();
                return;
            }

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            if (clicked.getType() == Material.LIME_WOOL) {
                game.hit();
            } else if (clicked.getType() == Material.RED_WOOL) {
                activeBlackjackGames.remove(player);
                game.stand();
            } else if (clicked.getType() == Material.GOLD_INGOT && game.canDoubleDown()) {
                activeBlackjackGames.remove(player);
                game.doubleDown();
            }
        } else if (title.equals("§0§lBaccarat Table")) {
            event.setCancelled(true);
        }
    }

    public static void removeActiveGame(Player player) {
        activeBlackjackGames.remove(player);
        activeBaccaratGames.remove(player);
    }
    
    public static void updateBlackjackGUI(Player player, Blackjack game) {
        if (player.getOpenInventory() != null && 
            player.getOpenInventory().getTitle().equals("§0§lBlackjack Table")) {
            updateBlackjackDisplay(player.getOpenInventory().getTopInventory(), game);
        }
    }
    
    public static void updateBaccaratGUI(Player player, Baccarat game) {
        if (player.getOpenInventory() != null && 
            player.getOpenInventory().getTitle().equals("§0§lBaccarat Table")) {
            updateBaccaratDisplay(player.getOpenInventory().getTopInventory(), game);
        }
    }
}
