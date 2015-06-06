package com.mcjailed.api.util;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.StructureModifier;
import com.mcjailed.api.MCJailed;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MetaFaker {

    private final ProtocolManager protocolManager;
    private final MetadataFilter filter;
    private final PacketListener listener;

    public MetaFaker(MetadataFilter filter) {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.filter = filter;
        this.listener = registerListener();
    }

    public void shutdown() {
        protocolManager.removePacketListener(listener);
    }

    private PacketListener registerListener() {
        PacketListener l = new PacketAdapter(MCJailed.getPlugin(), ConnectionSide.CLIENT_SIDE, 0x67, 0x68) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();

                switch (packet.getID()) {
                    case 0x67:
                        StructureModifier<ItemStack> sm = packet.getItemModifier();

                        for (int i = 0; i < sm.size(); i++) {
                            filterMetaData(sm.read(i), player);
                        }

                        break;
                    case 0x68:
                        StructureModifier<ItemStack[]> smArray = packet.getItemArrayModifier();

                        for (int i = 0; i < smArray.size(); i++) {
                            ItemStack[] stacks = smArray.read(i);

                            for (int n = 0; n < stacks.length; n++) {
                                filterMetaData(stacks[n], player);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        protocolManager.addPacketListener(l);

        return l;
    }

    private void filterMetaData(ItemStack stack, Player player) {
        if (stack != null) {
            ItemMeta newMeta = filter.filter(stack.getItemMeta(), player);

            if (newMeta != null) {
                stack.setItemMeta(newMeta);
            }
        }
    }

    public interface MetadataFilter {
        public ItemMeta filter(ItemMeta itemMeta, Player player);
    }

}