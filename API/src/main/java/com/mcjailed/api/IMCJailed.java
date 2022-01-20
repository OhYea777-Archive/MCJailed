package com.mcjailed.api;

import com.mcjailed.api.module.IModules;
import com.mcjailed.api.player.MCJailedPlayer;
import com.mcjailed.api.util.IGsonUtils;
import com.mcjailed.api.util.VaultRegistry;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public interface IMCJailed {

    /**
     * Returns a helper class for all of the Modules.
     */
    IModules getModules();

    /**
     * Returns the {@link org.bukkit.plugin.Plugin} for MCJailed.
     */
    Plugin getPlugin();

    /**
     * Returns a helper class for creating objects from, and saving objects to, files.
     */
    IGsonUtils getGsonUtils();

    /**
     * Returns a {@link MCJailedPlayer} instance of a Bukkit {@link org.bukkit.OfflinePlayer}.
     *
     * @param player The player.
     */
    MCJailedPlayer getPlayer(OfflinePlayer player);

    /**
     * Returns an instance of the {@link VaultRegistry}
     */
    VaultRegistry getVaultRegistry();

}
