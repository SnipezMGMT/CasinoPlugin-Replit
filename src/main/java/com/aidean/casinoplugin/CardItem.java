package com.aidean.casinoplugin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CardItem {
    
    public static ItemStack createCard(Card card) {
        Material material = getMaterialForCard(card);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        String suitColor = getSuitColor(card.getSuit());
        meta.setDisplayName(suitColor + card.getRank().getDisplay() + card.getSuit().getSymbol());
        meta.setLore(Arrays.asList(
            "§7Value: §f" + card.getValue(),
            "§7" + card.getSuit().name() + " " + card.getRank().name()
        ));
        
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createHiddenCard() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§8§l[Hidden Card]");
        meta.setLore(Arrays.asList("§7Face Down"));
        item.setItemMeta(meta);
        return item;
    }
    
    private static Material getMaterialForCard(Card card) {
        switch (card.getSuit()) {
            case HEARTS:
                return Material.RED_CONCRETE;
            case DIAMONDS:
                return Material.ORANGE_CONCRETE;
            case CLUBS:
                return Material.BLACK_CONCRETE;
            case SPADES:
                return Material.GRAY_CONCRETE;
            default:
                return Material.PAPER;
        }
    }
    
    private static String getSuitColor(Card.Suit suit) {
        switch (suit) {
            case HEARTS:
            case DIAMONDS:
                return "§c";
            case CLUBS:
            case SPADES:
                return "§8";
            default:
                return "§f";
        }
    }
}
