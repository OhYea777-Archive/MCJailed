package com.mcjailed.rankup.config;

import com.mcjailed.module.modules.ItemData;
import com.mcjailed.rankup.player.RankupPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class RankupConfig {

    private String prefix = "&8[&6MCJailed&8] ";
    private String rankup = "&eYou have just ranked up to &6%rank%&e!";
    private String broadcast = "&3%player%&b has just ranked up to &3%rank%&b!";
    private String notEnoughMoney = "&cYou need &4$%price%&c to rank up to &4%rank%&c!";
    private String economyError = "&cTransaction incomplete. Economy error. Please notify an Admin";
    private String canNotRankup = "&cYou are not able to rank up any further!";

    private List<Rank> ranks = new ArrayList<Rank>();
    private RanksGui ranksGui = new RanksGui();

    private static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public String getPrefix() {
        return format(prefix);
    }

    public String getRankup(Rank rank) {
        return getPrefix() + format(rankup.replace("%rank%", rank.getGroup()).replace("%price%", getBalanceFormatted(rank.getPrice())));
    }

    public String getBroadcast(String player, Rank rank) {
        return getPrefix() + format(broadcast.replace("%rank%", rank.getGroup()).replace("%price%", getBalanceFormatted(rank.getPrice())).replace("%player%", player));
    }

    public String getNotEnoughMoney(Rank rank) {
        return getPrefix() + format(notEnoughMoney.replace("%rank%", rank.getGroup()).replace("%price%", getBalanceFormatted(rank.getPrice())));
    }

    public String getEconomyError() {
        return getPrefix() + format(economyError);
    }

    public String getCanNotRankup() {
        return getPrefix() + format(canNotRankup);
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public RanksGui getRanksGui() {
        return ranksGui;
    }

    public int getInvSize() {
        return ((ranks.size() / 9) + 1) * 9;
    }

    private String getBalanceFormatted(double amount) {
        if (amount >= 1.0E12) {
            return String.format("%.2fT", amount / 1.0E12);
        } else if (amount >= 1.0E9) {
            return String.format("%.2fB", amount / 1.0E9);
        } else if (amount >= 1000000.0) {
            return String.format("%.2fM", amount / 1000000.0);
        }

        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    public class Rank {

        private String group;
        private double price;

        public Rank() { }

        public Rank(String group, double price) {
            this.group = group;
            this.price = price;
        }

        public String getGroup() {
            return group;
        }

        public double getPrice() {
            return price;
        }

    }

    public class RanksGui {

        private String title = "&6Ranks&8:";

        private RankType hasRank = new RankType("&6Rank &8(&aUnlocked&8):&r %prefix%", Arrays.asList("&aRank cost &2%cost%", "&eClick to be warped to &6%group%"), new SerializableItem(Material.STAINED_CLAY, (short) 5));
        private RankType currentRank = new RankType("&6Rank &8(&bCurrent&8):&r %prefix%", Arrays.asList("&bRank cost &3%cost%", "&eClick to be warped to &6%group%"), new SerializableItem(Material.STAINED_CLAY, (short) 3));
        private RankType rank = new RankType("&6Rank &8(&cLocked&8):&r %prefix%", Arrays.asList("&cRank costs &4%cost%&c!"), new SerializableItem(Material.STAINED_CLAY, (short) 14));

        public String getTitle() {
            return title;
        }

        public RankType getHasRank() {
            return hasRank;
        }

        public RankType getCurrentRank() {
            return currentRank;
        }

        public RankType getRank() {
            return rank;
        }

        public class RankType {

            private String title;
            private List<String> lore;
            private SerializableItem item;

            public RankType() { }

            public RankType(String title, List<String> lore, SerializableItem item) {
                this.title = title;
                this.lore = lore;
                this.item = item;
            }

            public String getTitle() {
                return format(title);
            }

            public List<String> getLore(Rank rank) {
                List<String> formattedLore = new ArrayList<String>();

                for (String msg : lore) {
                    formattedLore.add(format(msg.replace("%group%", rank.getGroup()).replace("%cost%", rank.getPrice() == 0 ? "Free" : ("$" + new DecimalFormat("#,###.00").format(rank.getPrice())))));
                }

                return formattedLore;
            }

            public ItemStack getItem(Rank rank, RankupPlayer player) {
                ItemStack formattedItem = item.getItem();
                ItemMeta meta = formattedItem.getItemMeta();

                meta.setDisplayName(format(getTitle().replace("%prefix%", player.getRankPrefix(rank))));
                meta.setLore(getLore(rank));
                formattedItem.setItemMeta(meta);
                ItemData.set(formattedItem, "rank", rank.getGroup());

                return formattedItem;
            }

        }

    }

    public class SerializableItem {

        private String material;
        private short damage;

        public SerializableItem() { }

        public SerializableItem(Material material, short damage) {
            this.material = material.name();
            this.damage = damage;
        }

        private Material getMaterial() {
            if (Material.getMaterial(material) == null) material = Material.AIR.name();

            return Material.getMaterial(material);
        }

        public ItemStack getItem() {
            return new ItemStack(getMaterial(), 1, damage);
        }

    }

}
