package com.mcjailed.api.util;

import com.mcjailed.api.util.nms.NMSClass;

public class NBTUtils {

    /**
     * Returns the {@link com.mcjailed.api.util.nms.NMSClass} for a NBTTagCompound.
     */
    public static NMSClass getNBTTagCompoundNMSClass() {
        return NMSUtils.getNMSClass("{nms}.NBTTagCompound");
    }

    /**
     * Returns a new instance of a NBTTagCompound.
     */
    public static Object createNBTTagCompound() {
        return getNBTTagCompoundNMSClass().newInstance();
    }

    /**
     * Converts a NBTTagCompound serialized to json as its NBTTagCompound equivalent.
     *
     * @param string The json of the NBTTagCompound.
     */
    public static Object toNBTTagCompound(String string) {
        return NMSUtils.getNMSClass("{nms}.MojangsonParser").getMethod("parse", String.class).call(StringUtils.format(string));
    }

    /**
     * Returns a given NBTTagCompound serialized to json.
     *
     * @param nbt The NBTTagCompound to be serialized.
     */
    public static String fromNBTTagCompound(Object nbt) {
        String fixedString = nbt.toString();

        for (int i = 0; i < nbt.toString().length(); i ++) {
            if (nbt.toString().charAt(i) == ',' && i + 1 < nbt.toString().length() && (nbt.toString().charAt(i + 1) == '}' || nbt.toString().charAt(i + 1) == ']'))
                continue;

            fixedString += nbt.toString().charAt(i);
        }

        return fixedString.replace('\u00A7', '&').replace("\"", "\\\"");
    }

}
