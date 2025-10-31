package com.aidean.casinoplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class CasinoPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("CasinoPlugin Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CasinoPlugin Disabled!");
    }
}