package com.mcjailed.api.util;

import com.mcjailed.api.MCJailed;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultRegistry {

    private Economy economy;
    private Chat chat;
    private Permission permission;

    public VaultRegistry() {
        if (checkServiceProvider(Economy.class))
            economy = getServiceProvider(Economy.class);

        if (checkServiceProvider(Chat.class))
            chat = getServiceProvider(Chat.class);

        if (checkServiceProvider(Permission.class))
            permission = getServiceProvider(Permission.class);
    }

    private <T> T getServiceProvider(Class<T> serviceProviderClass) {
        final RegisteredServiceProvider<T> rsp = MCJailed.getPlugin().getServer().getServicesManager().getRegistration(serviceProviderClass);

        return rsp.getProvider();
    }

    private <T> boolean checkServiceProvider(Class<T> serviceProviderClass) {
        final RegisteredServiceProvider<T> rsp = MCJailed.getPlugin().getServer().getServicesManager().getRegistration(serviceProviderClass);

        if (rsp == null) {
            MCJailed.getPlugin().getLogger().warning("[Vault Registry] Failed to find a service provider for '" + serviceProviderClass.getName() + "'");

            return false;
        } else
            return true;
    }

    public boolean isEconomyLoaded() {
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean isChatLoaded() {
        return chat != null;
    }

    public Chat getChat() {
        return chat;
    }

    public boolean isPermissionLoaded() {
        return permission != null;
    }

    public Permission getPermission() {
        return permission;
    }

}