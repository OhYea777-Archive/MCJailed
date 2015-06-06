package com.mcjailed;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.mcjailed.api.IMCJailed;
import com.mcjailed.api.MCJailed;
import com.mcjailed.api.module.IModule;
import com.mcjailed.api.module.IModules;
import com.mcjailed.api.player.MCJailedPlayer;
import com.mcjailed.api.player.network.play.PacketPlayOutChat;
import com.mcjailed.api.util.IGsonUtils;
import com.mcjailed.api.util.StringUtils;
import com.mcjailed.api.util.VaultRegistry;
import com.mcjailed.config.MCJailedConfig;
import com.mcjailed.module.Modules;
import com.mcjailed.module.loaders.ModuleClassLoader;
import com.mcjailed.module.loaders.ModuleJarLoader;
import com.mcjailed.module.loaders.ModuleReflectionsLoader;
import com.mcjailed.util.GsonUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class MCJailedCore extends JavaPlugin implements IMCJailed {

    private static MCJailedCore instance;

    private MCJailedConfig config;
    private IModules modules;
    private GsonUtils gsonUtils;

    private boolean isDatabaseCreated = false;
    private List<Class<?>> databaseClasses = new ArrayList<Class<?>>();

    private Map<UUID, MCJailedPlayer> players = new HashMap<UUID, MCJailedPlayer>();

    private VaultRegistry vaultRegistry;

    @Override
    public void onEnable() {
        instance = this;

        MCJailed.setMCJailed(instance);

        vaultRegistry = new VaultRegistry();
        config = getGsonUtils().createFromFile(new File(getDataFolder(), "config.json"), MCJailedConfig.class);
        modules = new Modules();

        modules.register(new ModuleClassLoader());
        modules.register(new ModuleJarLoader());
        modules.register(new ModuleReflectionsLoader());
        modules.loadAll(false);

        createDatabase();
        setupDatabase();

        getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
            @Override
            public void run() {
                modules.enableAllModules(true);
            }
        });
    }

    public void addDatabaseClasses(Collection<Class<?>> classes) {
        databaseClasses.addAll(classes);
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        return databaseClasses;
    }

    private String replaceDatabaseString(String input) {
        input = input.replaceAll("\\{DIR\\}", getDataFolder().getPath().replaceAll("\\\\", "/") + "/");
        input = input.replaceAll("\\{NAME\\}", getDescription().getName().replaceAll("[^\\w_-]", ""));
        return input;
    }

    private void createDatabase() {
        if (!isDatabaseCreated) {
            final ServerConfig db = new ServerConfig();
            db.setDefaultServer(false);
            db.setRegister(false);
            db.setClasses(getDatabaseClasses());
            db.setName(getDescription().getName());
            getServer().configureDbConfig(db);

            final DataSourceConfig ds = db.getDataSourceConfig();
            ds.setUrl(replaceDatabaseString(ds.getUrl()));
            getDataFolder().mkdirs();

            ClassLoader current = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(((Modules) modules).getClassLoader());
            EbeanServer ebean = EbeanServerFactory.create(db);
            Thread.currentThread().setContextClassLoader(current);

            try {
                Field field = JavaPlugin.class.getDeclaredField("ebean");
                field.setAccessible(true);
                field.set(this, ebean);
            } catch (Exception e) {
                e.printStackTrace();
            }

            isDatabaseCreated = true;
        }
    }

    public void setupDatabase() {
        try {
            for (Class<?> clazz : getDatabaseClasses()) getDatabase().find(clazz).findRowCount();
        } catch (PersistenceException e) {
            getLogger().info("Installing database due to first time usage");
            installDDL();
        }
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("modules")) {
            if (!sender.isOp()) {
                sender.sendMessage("Unknown command. Type \"/help\" for help.");

                return true;
            }

            if (args.length == 0) {
                sendModules(sender, 1);

                return true;
            } else if (args.length >= 2) {
                if (args.length == 2 && args[0].equalsIgnoreCase("page")) {
                    if (StringUtils.isNumber(args[1])) {
                        sendModules(sender, StringUtils.toNumber(args[1]));

                        return true;
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("enable")) {
                    if (getModules().moduleExists(StringUtils.toString(1, args))) {
                        IModule module = getModules().getModule(StringUtils.toString(1, args));

                        if (getModules().enableModule(module)) {
                            sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getModuleEnable().replace("{module}", StringUtils.toString(1, args))));
                        } else {
                            sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getModuleAlreadyEnabled().replace("{module}", StringUtils.toString(1, args))));
                        }
                    } else {
                        sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getModuleNotFound().replace("{module}", StringUtils.toString(1, args))));
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("disable")) {
                    if (getModules().moduleExists(StringUtils.toString(1, args))) {
                        IModule module = getModules().getModule(StringUtils.toString(1, args));

                        if (getModules().disableModule(module)) {
                            sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getModuleDisabled().replace("{module}", StringUtils.toString(1, args))));
                        } else {
                            sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getModuleAlreadyDisabled().replace("{module}", StringUtils.toString(1, args))));
                        }
                    } else {
                        sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getModuleNotFound().replace("{module}", StringUtils.toString(1, args))));
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (getModules().moduleExists(StringUtils.toString(1, args))) {
                        IModule module = getModules().getModule(StringUtils.toString(1, args));

                        getModules().reloadModule(module);
                        sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getModuleReloaded().replace("{module}", StringUtils.toString(1, args))));
                    } else {
                        sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getModuleNotFound().replace("{module}", StringUtils.toString(1, args))));
                    }

                    return true;
                }
            }

            sender.sendMessage(StringUtils.format(getActualConfig().getPrefix() + getActualConfig().getInvalidArgs()));

            return true;
        }

        return true;
    }

    private int getMaxPages() {
        return getModules().getModules().size() / getActualConfig().getModulesPerPage() + 1;
    }

    private void sendModules(CommandSender sender, int page) {
        page = page > getMaxPages() ? getMaxPages() : (page < 1 ? 1 : page);
        int max = page * 10;
        int min = max - 10;

        sender.sendMessage(StringUtils.format(getActualConfig().getModuleFormat().replace("{page}", "" + page).replace("{maxpages}", "" + getMaxPages())));

        List<IModule> modules = new ArrayList<IModule>();

        modules.addAll(getModules().getModules());

        for (int moduleId = min; moduleId < Math.min(max, modules.size()); moduleId ++) {
            IModule module = modules.get(moduleId);
            String moduleName = (module.isEnabled() ? "&a" : "&c") + module.getName();
            String def = StringUtils.format(getActualConfig().getBulletPoint() + moduleName);
            PacketPlayOutChat packet = createMessage(moduleName, "/modules " + (module.isEnabled() ? "disable" : "enable") + " " + module.getName());

            StringUtils.sendMessage(sender, def, packet);
        }
    }

    private PacketPlayOutChat createMessage(String moduleName, String command) {
        return new PacketPlayOutChat("{'text': '" + getActualConfig().getBulletPoint() + "', 'extra': [{'text': '" + moduleName + "', 'clickEvent': {'action': 'run_command', 'value': '" + command + "'}}]}", PacketPlayOutChat.ChatType.CHAT);
    }

    /**
     * Returns the main config for MCJailed.
     */
    public MCJailedConfig getActualConfig() {
        return config;
    }

    /**
     * Returns the current instance of MCJailed.
     */
    public static MCJailedCore getInstance() {
        return instance;
    }

    @Override
    public VaultRegistry getVaultRegistry() {
        return vaultRegistry;
    }

    @Override
    public IModules getModules() {
        return modules;
    }

    @Override
    public Plugin getPlugin() {
        return instance;
    }

    @Override
    public IGsonUtils getGsonUtils() {
        if (gsonUtils == null) gsonUtils = new GsonUtils();

        return gsonUtils;
    }

    @Override
    public MCJailedPlayer getPlayer(OfflinePlayer player) {
        if (!players.containsKey(player.getUniqueId())) players.put(player.getUniqueId(), new MCJailedPlayer(player));

        return players.get(player.getUniqueId());
    }

}
