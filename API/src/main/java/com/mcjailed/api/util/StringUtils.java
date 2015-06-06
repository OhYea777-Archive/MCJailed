package com.mcjailed.api.util;

import com.mcjailed.api.MCJailed;
import com.mcjailed.api.player.network.play.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

    /**
     * Returns a formatted string given a string.
     *
     * @param string The string to be formatted.
     */
    public static String format(String string) {
        return format('&', string);
    }

    /**
     * Returns a formatted string given a string and a format code.
     *
     * @param formatCode The code to be replaced during formatting
     * @param string The string to be formatted.
     */
    public static String format(char formatCode, String string) {
        return ChatColor.translateAlternateColorCodes(formatCode, string);
    }

    public static boolean isNumber(String number) {
        try {
            Integer.valueOf(number);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int toNumber(String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String toString(String[] args) {
        return toString(args, ' ');
    }

    public static String toString(int start, String[] args) {
        return toString(start, args.length, args);
    }

    public static String toString(int start, int end, String[] args) {
        return toString(Arrays.copyOfRange(args, start, end), ' ');
    }

    public static String toString(int start, String[] args, char separator) {
        return toString(start, args.length, args, separator);
    }

    public static String toString(int start, int end, String[] args, char separator) {
        return toString(Arrays.copyOfRange(args, start, end), separator);
    }

    public static String toString(String[] args, char separator) {
        StringBuilder string = new StringBuilder();

        if (args.length > 0) {
            string.append(args[0]);

            for (int i = 1; i < args.length; i ++) {
                string.append(separator).append(args[i]);
            }
        }

        return string.toString();
    }

    public static List<String> format(List<String> args) {
        return Arrays.asList(format(args.toArray(new String[args.size()])));
    }

    public static String[] format(String[] args) {
        String[] formatted = new String[args.length];

        for (int i = 0; i < args.length; i ++) {
            formatted[i] = format(args[i]);
        }

        return formatted;
    }

    public static void sendMessage(CommandSender sender, String message, PacketPlayOutChat chatPacket) {
        if (sender instanceof Player) {
            MCJailed.getPlayer((Player) sender).sendPacket(chatPacket);
        } else {
            sender.sendMessage(message);
        }
    }

    public static String convert(final String text) {
        if (text == null || text.length() == 0) {
            return "\"\"";
        }

        final int len = text.length();
        final StringBuilder sb = new StringBuilder(len + 4);

        sb.append('\"');

        for (int i = 0; i < len; ++i) {
            final char c = text.charAt(i);
            switch (c) {
                case '\b': {
                    sb.append("\\b");
                    break;
                }
                case '\t': {
                    sb.append("\\t");
                    break;
                }
                case '\n': {
                    sb.append("\\n");
                    break;
                }
                case '\f': {
                    sb.append("\\f");
                    break;
                }
                case '\r': {
                    sb.append("\\r");
                    break;
                }
                case '\"':
                case '\\': {
                    sb.append('\\');
                    sb.append(c);
                    break;
                }
                case '/': {
                    sb.append('\\');
                    sb.append(c);
                    break;
                }
                default: {
                    if (c < ' ') {
                        final String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                        break;
                    }
                    sb.append(c);
                    break;
                }
            }
        }

        return sb.append('\"').toString();
    }

}
