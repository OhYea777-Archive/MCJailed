package com.mcjailed.api.module;

import com.mcjailed.api.module.loaders.AbstractModuleLoader;

import java.util.Collection;
import java.util.Iterator;

public interface IModules extends Iterable<IModule> {

    /**
     * Registers a {@link com.mcjailed.api.module.loaders.AbstractModuleLoader}.
     *
     * @param loader For the modules to be loaded from.
     */
    void register(AbstractModuleLoader loader);

    /**
     * Loads all Modules for all registered {@link com.mcjailed.api.module.loaders.AbstractModuleLoader}s.
     *
     * @param enableModules Whether to enable the Modules after they are loaded.
     */
    void loadAll(boolean enableModules);

    /**
     * Registers a Module.
     *
     * @param module The Module to be registered.
     */
    void registerModule(IModule module);

    /**
     * Returns whether a Module exists by the given name.
     *
     * @param moduleName The name of the Module.
     */
    boolean moduleExists(String moduleName);

    /**
     * Returns whether a Module exists by the given class.
     *
     * @param moduleClass The class of the Module.
     */
    boolean moduleExists(Class<? extends IModule> moduleClass);

    /**
     * Enables a Module given its name.
     *
     * @param module The name of the Module.
     */
    boolean enableModule(String module);

    /**
     * Enables a given module.
     *
     * @param module The Module to be enabled.
     */
    boolean enableModule(IModule module);

    /**
     * Enables all Modules.
     *
     * @param ignoreDefaultDisabled Whether to ignore Modules disabled by default.
     */
    void enableAllModules(boolean ignoreDefaultDisabled);

    /**
     * Reloads a Module given its name.
     *
     * @param module The name of the Module.
     */
    void reloadModule(String module);

    /**
     * Reloads a given Module.
     *
     * @param module The module to be reloaded.
     */
    void reloadModule(IModule module);

    /**
     * Reloads all Modules.
     */
    void reloadAllModules();

    /**
     * Disables a Module given its name.
     *
     * @param module The name of the Module.
     */
    boolean disableModule(String module);

    /**
     * Disables a given Module.
     *
     * @param module The module to be disabled.
     */
    boolean disableModule(IModule module);

    void disableAllModules();

    /**
     * Returns a module given its name.
     *
     * @param moduleName The name of the Module.
     */
    IModule getModule(String moduleName);

    /**
     * Returns a Module given its class, name and type.
     *
     * @param moduleName The name of the Module.
     * @param moduleClass The class of the Module.
     * @param <T> The Module type.
     */
    <T extends IModule> T getModule(String moduleName, Class<T> moduleClass);

    /**
     * Returns a Module given its class and type.
     *
     * @param moduleClass The class of the Module.
     * @param <T> The Module type.
     */
    <T extends IModule> T getModule(Class<T> moduleClass);

    Collection<IModule> getModules();

    /**
     * Returns an {@link java.util.Iterator} for all of the Modules.
     */
    Iterator<IModule> iterator();

}
