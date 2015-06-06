package com.mcjailed.api.util.serialization;

import com.avaje.ebean.validation.NotEmpty;
import com.mcjailed.api.util.NBTUtils;
import com.mcjailed.api.util.NMSUtils;
import org.bukkit.inventory.ItemStack;

public class SerializableItemStack {

    @NotEmpty
    private String item;

    public SerializableItemStack(ItemStack itemStack) {
        Object minecraftItemStack = NMSUtils.getNMSClass("{cb}.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).call(itemStack);
        Object nbt = NBTUtils.createNBTTagCompound();

        NMSUtils.getNMSClass("{nms}.ItemStack").getMethod("save", NBTUtils.getNBTTagCompoundNMSClass()).of(minecraftItemStack).call(nbt);

        item = NBTUtils.fromNBTTagCompound(nbt);
    }

    /**
     * Returns the json for the serialized {@link org.bukkit.inventory.ItemStack}.
     */
    public String getItem() {
        return item;
    }

    /**
     * Sets the json for the serialized {@link org.bukkit.inventory.ItemStack}.
     *
     * @param item The item's json.
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * Translates the item's json to an {@link org.bukkit.inventory.ItemStack} and returns it.
     */
    public ItemStack getItemStack() {
        Object nbt = NBTUtils.toNBTTagCompound(item);
        Object minecraftItemStack = NMSUtils.getNMSClass("{nms}.ItemStack").getMethod("createStack", NMSUtils.getNMSClass("{nms}.NBTTagCompound")).call(nbt);

        return (ItemStack) NMSUtils.getNMSClass("{cb}.CraftItemStack").getMethod("asBukkitCopy", NMSUtils.getNMSClass("{nms}.ItemStack")).call(minecraftItemStack);
    }

}
