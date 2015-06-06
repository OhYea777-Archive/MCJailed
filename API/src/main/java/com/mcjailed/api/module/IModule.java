package com.mcjailed.api.module;

import com.mcjailed.api.IMCJailed;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface IModule<T> extends ICommandRegistry {

    /**
     * Returns the name of the Module.
     */
    String getName();

    /**
     * Returns the class for the Module's config. Usually returns null if the Module has no config.
     */
    Class<T> getConfigClass();

    /**
     * Returns whether the Module has a config.
     */
    boolean hasConfig();

    /**
     * Returns the name of the Module's config. Used for saving the Module's config.
     */
    String getConfigName();

    /**
     * Returns the classes the Module's uses for database storage.
     */
    List<Class<?>> getDatabaseClasses();

    /**
     * Initializes the Module with a config.
     * @param config The config for the Module.
     */
    void onEnable(T config);

    /**
     * Initializes the Module.
     */
    void onEnable();

    /**
     * Reloads the Module's config.
     *
     * @return A new, updated instance of the Module's config.
     */
    T reloadConfig();

    /**
     * Reloads the Module.
     */
    void onReload();

    /**
     * De-initializes the Module.
     */
    void onDisable();

    /**
     * Saves a Module's config.
     *
     * @param config The Module's config to be saved.
     */
    void saveConfig(T config);

    /**
     * Returns whether the Module is enabled.
     */
    boolean isEnabled();

    /**
     * Sets whether the module is enabled.
     *
     * @param isEnabled Whether the module is enabled.
     */
    void setEnabled(boolean isEnabled);

    /**
     * Returns an instance for an {@link com.mcjailed.api.module.ICommandRegistry}.
     */
    ICommandRegistry getCommandRegistry();

    /**
     * Sets the instance for an {@link com.mcjailed.api.module.ICommandRegistry}.
     *
     * @param commandRegistry The instance of the {@link com.mcjailed.api.module.ICommandRegistry}.
     */
    void setCommandRegistry(ICommandRegistry commandRegistry);

    /**
     * Returns an instance of the {@link IMCJailed} plugin.
     */
    Plugin getPlugin();

}
