package com.mcjailed.rankup.player;

import com.mcjailed.api.MCJailed;
import com.mcjailed.api.util.StringUtils;
import com.mcjailed.rankup.Rankup;
import com.mcjailed.rankup.config.RankupConfig.Rank;
import com.mcjailed.api.player.MCJailedPlayer;
import com.mcjailed.api.util.RandomFirework;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class RankupPlayer {

    private Player player;
    private MCJailedPlayer mcjailedPlayer;

    public RankupPlayer(Player player) {
        this.player = player;
        this.mcjailedPlayer = new MCJailedPlayer(player);
    }

    public boolean isRankupEnabled() {
        return MCJailed.getModules().moduleExists("Rankup") && MCJailed.getModules().getModule("Rankup").isEnabled();
    }

    public Rankup getRankup() {
        if (isRankupEnabled()) {
            return (Rankup) MCJailed.getModules().getModule("Rankup");
        }

        return null;
    }

    public MCJailedPlayer getMCJailedPlayer() {
        return mcjailedPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getCurrentRank() {
        if (isRankupEnabled() && getMCJailedPlayer().isPermissionsExEnabled()) {
            for (Rank rank : getRankup().getConfig().getRanks()) {
                if (getMCJailedPlayer().getPermissionsExUser().inGroup(rank.getGroup(), false)) {
                    return rank;
                }
            }
        }

        return null;
    }

    public Rank getNextRank() {
        if (isRankupEnabled() && getMCJailedPlayer().isPermissionsExEnabled()) {
            boolean first = false;

            for (Rank rank : getRankup().getConfig().getRanks()) {
                if (first) {
                    return rank;
                }
                if (getMCJailedPlayer().getPermissionsExUser().inGroup(rank.getGroup(), false)) first = true;
            }
        }

        return null;
    }

    public String getRankPrefix(Rank rank) {
        if (rank != null) {
            return getMCJailedPlayer().getPrefix(rank.getGroup());
        }

        return "";
    }

    public boolean rankup() {
        if (isRankupEnabled()) {
            Rank currentRank = getCurrentRank();
            Rank rank = getNextRank();

            if (currentRank != null && rank != null) {
                if (getMCJailedPlayer().has(rank.getPrice())) {
                    if (getMCJailedPlayer().withdraw(rank.getPrice()).transactionSuccess()) {
                        sendTitle("&bCongrats!");
                        sendSubtitle(getRankup().getConfig().getRankup(rank));

                        player.sendMessage(getRankup().getConfig().getRankup(rank));
                        player.getServer().broadcastMessage(getRankup().getConfig().getBroadcast(player.getName(), rank));

                        getMCJailedPlayer().getPermissionsExUser().addGroup(rank.getGroup());
                        getMCJailedPlayer().getPermissionsExUser().removeGroup(currentRank.getGroup());

                        RandomFirework.spawnRandomFirework(player.getLocation());

                        return true;
                    } else {
                        player.sendMessage(getRankup().getConfig().getEconomyError());
                    }
                } else {
                    player.sendMessage(getRankup().getConfig().getNotEnoughMoney(rank));
                }
            } else {
                player.sendMessage(getRankup().getConfig().getCanNotRankup());
            }
        }

        return false;
    }

    private void send(String raw, EnumTitleAction action) {
        IChatBaseComponent subtitle = null;

        if (raw.startsWith("{") && raw.endsWith("}")) {
            subtitle = ChatSerializer.a(raw);
        } else {
            final String titleStr = StringUtils.format(raw);
            subtitle = ChatSerializer.a(StringUtils.convert(titleStr));
        }

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        PacketPlayOutTitle packet = new PacketPlayOutTitle(action, subtitle);

        entityPlayer.playerConnection.sendPacket(packet);
    }

    public void sendTitle(String title) {
        send(title, EnumTitleAction.TITLE);

        try {
            if (MCJailed.getModules().moduleExists("Scoreboard Manager") && MCJailed.getModules().getModule("Scoreboard Manager").isEnabled()) {
                Method unExemptPlayer = MCJailed.getModules().getModule("Scoreboard Manager").getClass().getMethod("unExemptPlayer", Player.class);
                Method exemptPlayer = MCJailed.getModules().getModule("Scoreboard Manager").getClass().getMethod("exemptPlayer", Player.class, long.class);

                unExemptPlayer.invoke(MCJailed.getModules().getModule("Scoreboard Manager"), player);
                exemptPlayer.invoke(MCJailed.getModules().getModule("Scoreboard Manager"), player, 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSubtitle(String subtitle) {
        send(subtitle, EnumTitleAction.SUBTITLE);
    }

}
