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
import java.util.Map;

public class GUIManager implements Listener {
    private static final Map<Player, Blackjack> activeBlackjackGames = new HashMap<>();

    public static void openBlackjackGUI(Player player, Blackjack game) {
        activeBlackjackGames.put(player, game);
        
        Inventory gui = Bukkit.createInventory(null, 9, "§0§lBlackjack");

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

        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§e§lGame Info");
        infoMeta.setLore(Arrays.asList(
            "§7Click HIT to draw a card",
            "§7Click STAND to end your turn",
            "§7Get as close to 21 as possible!"
        ));
        info.setItemMeta(infoMeta);

        gui.setItem(2, hit);
        gui.setItem(4, info);
        gui.setItem(6, stand);

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        
        if (event.getView().getTitle().equals("§0§lBlackjack")) {
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
            }
        }
    }

    public static void removeActiveGame(Player player) {
        activeBlackjackGames.remove(player);
    }
}
