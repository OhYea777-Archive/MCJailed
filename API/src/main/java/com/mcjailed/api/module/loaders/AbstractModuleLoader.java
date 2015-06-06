package com.mcjailed.api.module.loaders;

import com.mcjailed.api.MCJailed;
import com.mcjailed.api.module.IModule;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public abstract class AbstractModuleLoader {

    private URL[] urls;

    public AbstractModuleLoader() { }

    public AbstractModuleLoader(File... jars) {
        try {
            if (jars != null) {
                URL[] urls = new URL[jars.length];

                for (int i = 0; i < jars.length; i ++) urls[i] = jars[i].toURI().toURL();

                this.urls = urls;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractModuleLoader(URL... urls) {
        this.urls = urls;
    }

    /**
     * Returns an array of {@link java.net.URL}s that this AbstractModuleLoader needs to load its Modules.
     */
    public URL[] getUrls() {
        return urls;
    }

    /**
     * Returns a list of modules that have been loaded into the classpath.
     *
     * @param classLoader The {@link java.net.URLClassLoader} for the Modules to be loaded from.
     */
    public abstract List<IModule> onLoad(URLClassLoader classLoader);

    /**
     * Returns an instance of the MCJailed plugin.
     */
    public Plugin getPlugin() {
        return MCJailed.getPlugin();
    }

}
