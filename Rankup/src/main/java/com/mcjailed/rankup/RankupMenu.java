package com.mcjailed.rankup;

import com.mcjailed.module.modules.ItemData;
import com.mcjailed.rankup.config.RankupConfig;
import com.mcjailed.rankup.player.RankupPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RankupMenu implements Listener {

    private String name;
    private int size;
    private Rankup rankup;
    private RankupPlayer player;
    private List<ItemStack> options;

    public static RankupMenu createFromPlayer(RankupPlayer player) {
        Rankup rankup = player.getRankup();
        String name = format(rankup.getConfig().getRanksGui().getTitle());
        int size = rankup.getConfig().getInvSize();
        List<ItemStack> options = new ArrayList<ItemStack>();
        RankupConfig.Rank currentRank = player.getCurrentRank();
        boolean isRank = true;

        for (RankupConfig.Rank rank : rankup.getConfig().getRanks()) {
            if (currentRank.getGroup().equals(rank.getGroup())) {
                options.add(rankup.getConfig().getRanksGui().getCurrentRank().getItem(rank, player));
            } else if (isRank) {
                options.add(rankup.getConfig().getRanksGui().getHasRank().getItem(rank, player));
            } else {
                options.add(rankup.getConfig().getRanksGui().getRank().getItem(rank, player));
            }

            if (currentRank != null && rank.getGroup() == currentRank.getGroup()) isRank = false;
        }

        return new RankupMenu(name, size, rankup, player, options);
    }

    public RankupMenu(String name, int size, Rankup rankup, RankupPlayer player, List<ItemStack> options) {
        this.name = name;
        this.size = size;
        this.rankup = rankup;
        this.player = player;
        this.options = options;

        rankup.getPlugin().getServer().getPluginManager().registerEvents(this, rankup.getPlugin());
        open();
    }

    private void open() {
        final Inventory inventory = rankup.getPlugin().getServer().createInventory(player.getPlayer(), size, name);

        inventory.setContents(options.toArray(new ItemStack[options.size()]));
        player.getPlayer().openInventory(inventory);
    }

    private void destroy() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && !event.getClick().equals(ClickType.MIDDLE) && !event.getCurrentItem().getType().equals(Material.AIR) && event.getInventory().getName().equals(name) && event.getWhoClicked() != null && event.getWhoClicked().getUniqueId().equals(player.getPlayer().getUniqueId())) {
            ItemStack itemStack = event.getCurrentItem();

            if (event.getClickedInventory().getName().equals(name) && ItemData.has(itemStack, "rank")) {
                event.setCancelled(true);

                for (RankupConfig.Rank rank : rankup.getConfig().getRanks()) {
                    if (rank.getGroup().equals(ItemData.get(itemStack, "rank"))) {
                        rankup.getPlugin().getServer().dispatchCommand(player.getPlayer(), "warp " + rank.getGroup());
                        event.getWhoClicked().closeInventory();
                    }

                    if (rank.getGroup().equals(player.getCurrentRank().getGroup())) return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getName() == name && event.getPlayer() != null && (event.getPlayer() instanceof Player) && ((Player) event.getPlayer()).getUniqueId() == player.getPlayer().getUniqueId()) {
            destroy();
        }
    }

    private static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
