package com.mcjailed.module;

import com.mcjailed.api.module.Cmd;
import com.mcjailed.api.module.ICommandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class CommandRegistry implements ICommandRegistry {

    private CommandMap commandMap;

    private Map<String, CommandHandler> commands = new HashMap<String, CommandHandler>();
    private Map<Object, List<String>> commandHandlers = new HashMap<Object, List<String>>();

    public CommandRegistry() {
        try{
            if(Bukkit.getServer() instanceof CraftServer){
                final Field f = CraftServer.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap)f.get(Bukkit.getServer());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void registerCommandHandler(Object commandHandler) {
        Class<?> clazz = commandHandler.getClass();
        List<String> commandNames = new ArrayList<String>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Cmd.class) && method.getReturnType().getName().equals("boolean") && method.getParameterTypes() != null && method.getParameterTypes().length == 4 && method.getParameterTypes()[0].equals(CommandSender.class) && method.getParameterTypes()[1].equals(Command.class) && method.getParameterTypes()[2].equals(String.class) && method.getParameterTypes()[3].equals(String[].class)) {
                Cmd cmd = method.getAnnotation(Cmd.class);

                if (!commands.containsKey(cmd.value().toLowerCase())) {
                    commands.put(cmd.value().toLowerCase(), new CommandHandler(commandHandler, method));
                    commandNames.add(cmd.value().toLowerCase());

                    registerCommand(cmd.value(), cmd.aliases());
                }
            }
        }

        if (commandNames.size() > 0) commandHandlers.put(commandHandler, commandNames);
    }

    @Override
    public void deregisterCommandHandler(Object commandHandler) {
        List<String> commandNames = commandHandlers.remove(commandHandler);

        if (commandNames != null) for (String commandName : commandNames) commands.remove(commandName);
    }

    public void registerCommand(String command, String... aliases) {
        if (commandMap != null) commandMap.register("mcjailed", new CCommand(command, aliases));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (commands.containsKey(command.getName().toLowerCase())) {
            CommandHandler commandHandler = commands.get(command.getName().toLowerCase());

            return commandHandler.onCommand(sender, command, label, args);
        }

        sender.sendMessage("Unknown command. Type \"/help\" for help.");

        return true;
    }

    private class CommandHandler {

        private final Object commandHandler;
        private final Method method;

        public CommandHandler(Object commandHandler, Method method) {
            this.commandHandler = commandHandler;
            this.method = method;
        }

        /**
         * Called when a registered command is ran.
         *
         * @param sender The sender of the command.
         * @param command The command itself.
         * @param label The label of the command.
         * @param args The args of the command.
         */
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            try {
                return (Boolean) method.invoke(commandHandler, sender, command, label, args);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

    }

    private class CCommand extends Command {

        public CCommand(String command, String... aliases) {
            super(command);

            if (aliases != null) setAliases(Arrays.asList(aliases));
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            return onCommand(sender, this, commandLabel, args);
        }

    }

}
