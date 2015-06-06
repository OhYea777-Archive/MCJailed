package com.mcjailed.api.player;

import com.mcjailed.api.MCJailed;
import com.mcjailed.api.player.network.Packet;
import com.mcjailed.api.util.NMSUtils;
import com.mcjailed.api.util.VaultRegistry;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class MCJailedPlayer {

    private UUID id;

    private transient OfflinePlayer player;

    public MCJailedPlayer(OfflinePlayer player) {
        this.id = player.getUniqueId();

        this.player = player;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public Player getPlayer() {
        return player.getPlayer();
    }

    public Object getPlayerConnection() {
        Player player = getPlayer();
        Object entityPlayer = NMSUtils.getNMSClass("{cb}.entity.CraftPlayer").getMethod("getHandle").of(player).call();

        return NMSUtils.getNMSClass("{nms}.EntityPlayer").getField("playerConnection").of(entityPlayer).get();
    }

    public void sendPacket(Packet packet) {
        if (player.isOnline()) {
            Object actualPacket = packet.getPacketObject();
            Object playerConnection = getPlayerConnection();

            NMSUtils.getNMSClass("{nms}.PlayerConnection").getMethod("sendPacket", NMSUtils.getNMSClass("{nms}.Packet")).of(playerConnection).call(actualPacket);
        }
    }

    public VaultRegistry getVaultRegistry() {
        return MCJailed.getVaultRegistry();
    }

    public boolean isPermissionsExEnabled() {
        return PermissionsEx.isAvailable();
    }

    public PermissionUser getPermissionsExUser() {
        return isPermissionsExEnabled() ? PermissionsEx.getUser(getPlayer()) : null;
    }

    public String getPrefix() {
        return isPermissionsExEnabled() ? getPermissionsExUser().getPrefix(getPlayer().getWorld().getName()) : null;
    }

    public String getPrefix(String group) {
        return isPermissionsExEnabled() ? PermissionsEx.getPermissionManager().getGroup(group).getPrefix(getPlayer().getWorld().getName()) : null;
    }

    public boolean isEconomyEnabled() {
        return getVaultRegistry().isEconomyLoaded();
    }

    public Economy getEconomy() {
        return isEconomyEnabled() ? getVaultRegistry().getEconomy() : null;
    }

    public double getBalance() {
        return isEconomyEnabled() ? getEconomy().getBalance(player) : 0;
    }

    public boolean has(double amount) {
        return getEconomy().has(player, amount);
    }

    public EconomyResponse withdraw(double amount) {
        return getEconomy().withdrawPlayer(player, amount);
    }

    public String getBalanceFormatted() {
        return getBalanceFormatted(getBalance());
    }

    public String getBalanceFormatted(double amount) {
        if (amount >= 1.0E12) {
            return String.format("%.2fT", amount / 1.0E12);
        } else if (amount >= 1.0E9) {
            return String.format("%.2fB", amount / 1.0E9);
        } else if (amount >= 1000000.0) {
            return String.format("%.2fM", amount / 1000000.0);
        }

        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }


}
