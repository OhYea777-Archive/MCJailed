package com.mcjailed.module;

import com.mcjailed.MCJailedCore;
import com.mcjailed.api.module.IModule;
import com.mcjailed.api.module.IModules;
import com.mcjailed.api.module.loaders.AbstractModuleLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Modules implements IModules {

    private List<AbstractModuleLoader> moduleLoaders = new ArrayList<AbstractModuleLoader>();

    private Map<String, IModule> modules = new HashMap<String, IModule>();
    private CommandRegistry commandRegistry = new CommandRegistry();

    private URLClassLoader classLoader;

    @Override
    public void register(AbstractModuleLoader loader) {
        moduleLoaders.add(loader);
    }

    @Override
    public void loadAll(boolean enableModules) {
        List<IModule> modulesToLoad = new ArrayList<IModule>();
        List<URL> urls = new ArrayList<URL>();

        for (AbstractModuleLoader loader : moduleLoaders) if (loader.getUrls() != null) urls.addAll(Arrays.asList(loader.getUrls()));

        try {
            classLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());

            for (AbstractModuleLoader loader : moduleLoaders) modulesToLoad.addAll(loader.onLoad(classLoader));

            for (IModule module : modulesToLoad) {
                registerModule(module);
                MCJailedCore.getInstance().getLogger().info("[Module Loader] Loaded module '" + module.getName() + "'!");

                if (module.getDatabaseClasses() != null && module.getDatabaseClasses().size() > 0) MCJailedCore.getInstance().addDatabaseClasses(module.getDatabaseClasses());
            }

            if (enableModules) enableAllModules(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerModule(IModule module) {
        modules.put(module.getName().toLowerCase(), module);
    }

    @Override
    public boolean moduleExists(String moduleName) {
        return modules.containsKey(moduleName.toLowerCase());
    }

    @Override
    public boolean moduleExists(Class<? extends IModule> moduleClass) {
        for (IModule module : this) if (module.getClass().equals(moduleClass)) return true;

        return false;
    }

    @Override
    public boolean enableModule(String module) {
        if (moduleExists(module)) return enableModule(getModule(module));

        return false;
    }

    @Override
    public boolean enableModule(IModule module) {
        if (!module.isEnabled()) {
            module.setCommandRegistry(commandRegistry);
            module.onEnable();
            module.setEnabled(true);

            return true;
        }

        return false;
    }

    @Override
    public void enableAllModules(boolean ignoreDefaultDisabled) {
        List<String> ignoredModules = new ArrayList<String>();

        if (!ignoreDefaultDisabled) ignoredModules = MCJailedCore.getInstance().getActualConfig().getDefaultDisabledModules();

        for (IModule module : this) if (!ignoredModules.contains(module.getName().toLowerCase())) enableModule(module);
    }

    @Override
    public void reloadModule(String module) {
        if (moduleExists(module)) reloadModule(getModule(module));
    }

    @Override
    public void reloadModule(IModule module) {
        module.onReload();
    }

    @Override
    public void reloadAllModules() {
        for (IModule module : this) reloadModule(module);
    }

    @Override
    public boolean disableModule(String module) {
        if (moduleExists(module)) return disableModule(getModule(module));

        return false;
    }

    @Override
    public boolean disableModule(IModule module) {
        if (module.isEnabled()) {
            module.onDisable();
            module.setEnabled(false);

            return true;
        }

        return false;
    }

    @Override
    public void disableAllModules() {
        for (IModule module : this) disableModule(module);
    }

    @Override
    public IModule getModule(String moduleName) {
        return modules.get(moduleName.toLowerCase());
    }

    @Override
    public <T extends IModule> T getModule(String moduleName, Class<T> moduleClass) {
        if (moduleExists(moduleName)) {
            IModule module = getModule(moduleName);

            if (module.getClass().equals(moduleClass)) return (T) module;
        }

        return null;
    }

    @Override
    public <T extends IModule> T getModule(Class<T> moduleClass) {
        for (IModule module : this) if (module.getClass().equals(moduleClass)) return (T) module;

        return null;
    }

    @Override
    public Collection<IModule> getModules() {
        return modules.values();
    }

    @Override
    public Iterator<IModule> iterator() {
        return modules.values().iterator();
    }

    public URLClassLoader getClassLoader() {
        return classLoader;
    }

}
