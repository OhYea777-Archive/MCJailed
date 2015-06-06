package com.mcjailed.module.modules;

import com.mcjailed.api.module.AbstractModule;
import com.mcjailed.api.util.MetaFaker;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemData extends AbstractModule implements MetaFaker.MetadataFilter {

    private static String PREFIX = "@#$%$#@]";
    private static String SPLITTER = ":";

    private MetaFaker metaFaker;

    @Override
    public String getName() {
        return "Item Data";
    }

    @Override
    public void onEnable() {
        super.onEnable();

        metaFaker = new MetaFaker(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        metaFaker.shutdown();
    }

    @Override
    public ItemMeta filter(ItemMeta itemMeta, Player player) {
        if (itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();

            for (String raw : itemMeta.getLore()) {
                if(raw.startsWith(PREFIX)) lore.remove(raw);
            }

            itemMeta.setLore(lore);
        }

        return itemMeta;
    }

    public static ItemStack set(ItemStack itemStack, String key, String value) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore() == null ? new ArrayList<String>() : meta.getLore();

        lore.add(PREFIX + key + SPLITTER + value);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public static boolean has(ItemStack itemStack, String key) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore())
            for (String lore : itemStack.getItemMeta().getLore()) {
                if (lore.startsWith(PREFIX)) {
                    String[] args = lore.replace(PREFIX, "").split(SPLITTER);

                    if (args.length == 2 && args[0].equalsIgnoreCase(key)) return true;
                }
            }

        return false;
    }

    public static String get(ItemStack itemStack, String key) {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore())
            for (String lore : itemStack.getItemMeta().getLore()) {
                if (lore.startsWith(PREFIX)) {
                    String[] args = lore.replace(PREFIX, "").split(SPLITTER);

                    if (args.length == 2 && args[0].equalsIgnoreCase(key)) return args[1];
                }
            }

        return "";
    }

}