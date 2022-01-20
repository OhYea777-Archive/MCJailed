package com.mcjailed.api;

import com.mcjailed.api.module.IModules;
import com.mcjailed.api.player.MCJailedPlayer;
import com.mcjailed.api.util.IGsonUtils;
import com.mcjailed.api.util.VaultRegistry;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class MCJailed {

    private static IMCJailed instance;

    /**
     * Returns a helper class for all of the Modules.
     */
    public static IModules getModules() {
        return instance.getModules();
    }

    /**
     * Returns the {@link org.bukkit.plugin.Plugin} for MCJailed.
     */
    public static Plugin getPlugin() {
        return instance.getPlugin();
    }

    /**
     * Returns a helper class for creating objects from, and saving objects to, files.
     */
    public static IGsonUtils getGsonUtils() {
        return instance.getGsonUtils();
    }

    /**
     * Returns a {@link MCJailedPlayer} instance of a Bukkit {@link org.bukkit.OfflinePlayer}.
     *
     * @param player The player.
     */
    public static MCJailedPlayer getPlayer(OfflinePlayer player) {
        return instance.getPlayer(player);
    }

    /**
     * Returns an instance of the {@link VaultRegistry}
     */
    public static VaultRegistry getVaultRegistry() {
        return instance.getVaultRegistry();
    }

    /**
     * Returns an instance of MCJailed.
     */
    public static IMCJailed getMCJailed() {
        return instance;
    }

    /**
     * Sets the instance for MCJailed.
     *
     * @param instance The instance of the plugin.
     */
    public static void setMCJailed(IMCJailed instance) {
        MCJailed.instance = instance;
    }

}
