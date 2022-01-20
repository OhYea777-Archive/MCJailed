package com.mcjailed.api.util;

import com.mcjailed.api.util.nms.NMSClass;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Method;

public class NMSUtils {

    private static String preClassBukkit = "org.bukkit.craftbukkit";
    private static String preClassMinecraft = "net.minecraft.server";
    private static boolean forge = false;

    static {
        if (Bukkit.getServer() != null) {
            if (Bukkit.getVersion().contains("MCPC") || Bukkit.getVersion().contains("Cauldron") || Bukkit.getVersion().contains("Forge")) forge = true;

            Server server = Bukkit.getServer();
            Class<?> bukkitServerClass = server.getClass();
            String[] parts = bukkitServerClass.getName().split("\\.");

            if (parts.length == 5) {
                String bukkitVersion = parts[3];
                preClassBukkit += "." + bukkitVersion;
            }

            try {
                Method getHandle = bukkitServerClass.getDeclaredMethod("getHandle");
                Object handle = getHandle.invoke(server);
                Class<?> handleServerClass = handle.getClass();
                parts = handleServerClass.getName().split("\\.");

                if (parts.length == 5) {
                    String minecraftVersion = parts[3];
                    preClassMinecraft += "." + minecraftVersion;
                }
            } catch (Exception ignored) { }
        }
    }

    /**
     * Returns whether the current Minecraft server software is Forge.
     */
    public static boolean isForge() {
        return forge;
    }

    /**
     * Returns a {@link com.mcjailed.api.util.nms.NMSClass} for a class given its name.
     *
     * @param classes The known classes for a given class.
     */
    public static NMSClass getNMSClass(String... classes) {
        for (String className : classes) {
            try {
                className = className.replace("{cb}", preClassBukkit).replace("{nms}", preClassMinecraft).replace("{nm}", "net.minecraft");

                return getNMSClass(Class.forName(className));
            } catch (ClassNotFoundException ignored) { }
        }

        throw new RuntimeException("[NMSUtils] No class found");
    }

    /**
     * Returns a {@link com.mcjailed.api.util.nms.NMSClass} for a given class.
     *
     * @param clazz The class to be wrapped as a {@link com.mcjailed.api.util.nms.NMSClass}.
     */
    public static NMSClass getNMSClass(Class<?> clazz) {
        return new NMSClass(clazz);
    }

    /**
     * Returns a new instance of a Chat Component given a message.
     *
     * @param message The message for the Chat Component to be made from.
     */
    public static Object createChatComponent(String message) {
        if (message.contains("{") && message.contains("}")) message = StringUtils.format(message);
        else message = "{\"text\": \"" + StringUtils.format(message) + "\"}";

        return getNMSClass("{nms}.ChatSerializer").getMethod("a", String.class).call(message);
    }

}
