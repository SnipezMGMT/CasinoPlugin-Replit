package com.aidean.casinoplugin;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CasinoPlugin extends JavaPlugin {
    private Economy economy;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("blackjack").setExecutor(new BlackjackCommand(this));
        getCommand("baccarat").setExecutor(new BaccaratCommand(this));
        
        getServer().getPluginManager().registerEvents(new GUIManager(), this);

        getLogger().info("CasinoPlugin Enabled!");
        getLogger().info("Economy system connected: " + economy.getName());
    }

    @Override
    public void onDisable() {
        getLogger().info("CasinoPlugin Disabled!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }
}
