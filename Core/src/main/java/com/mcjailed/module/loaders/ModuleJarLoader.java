package com.mcjailed.module.loaders;

import com.mcjailed.MCJailedCore;
import com.mcjailed.libs.org.apache.commons.io.IOUtils;
import com.mcjailed.api.module.AbstractModule;
import com.mcjailed.api.module.IModule;
import com.mcjailed.api.module.loaders.AbstractModuleLoader;
import com.mcjailed.util.GsonUtils;
import sun.misc.JarFilter;

import java.io.File;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleJarLoader extends AbstractModuleLoader {

    public ModuleJarLoader() {
        super(MCJailedCore.getInstance().getActualConfig().getModuleJarsDir().listFiles(new JarFilter()));
    }

    @Override
    public List<IModule> onLoad(URLClassLoader classLoader) {
        List<IModule> modules = new ArrayList<IModule>();
        File file = ((MCJailedCore) getPlugin()).getInstance().getActualConfig().getModuleJarsDir();

        if (file.exists() && file.isDirectory()) {
            try {
                for (File jarFile : getJars()) {
                    if (jarFile.isFile()) {
                        JarFile jar = new JarFile(jarFile);
                        JarEntry entry = jar.getJarEntry("module.json");

                        if (entry != null && !entry.isDirectory()) {
                            InputStream inputStream = jar.getInputStream(entry);

                            try {
                                String json = IOUtils.toString(inputStream);
                                ModuleMeta meta = GsonUtils.getGson().fromJson(json, ModuleMeta.class);
                                String main = meta.main;

                                if (main != null && !main.isEmpty()) {
                                    try {
                                        Class<?> clazz = classLoader.loadClass(main);

                                        if (clazz != null && AbstractModule.class.isAssignableFrom(clazz)) {
                                            modules.add((AbstractModule) clazz.newInstance());
                                        } else {
                                            getPlugin().getLogger().warning("[Module Loader] Could not load jar '" + jarFile.getName() + "' because its 'main' class is not a valid 'Module' sub-class!");
                                        }
                                    } catch (Exception ignored) {
                                        getPlugin().getLogger().warning("[Module Loader] Could not load jar'" + jarFile.getName() + "' because it does not have a 'main' class is not a class!");
                                    }
                                } else {
                                    getPlugin().getLogger().warning("[Module Loader] Could not load jar'" + jarFile.getName() + "' because it does not have a 'main' class set in its 'module.json' file!");
                                }
                            } catch (Exception ignored) {
                                getPlugin().getLogger().warning("[Module Loader] Could not load jar'" + jarFile.getName() + "' because its 'module.json' file is corrupt!");
                            }

                            inputStream.close();
                        } else {
                            getPlugin().getLogger().warning("[Module Loader] Could not load jar'" + jarFile.getName() + "' because it does not have a 'module.json' file!");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return modules;
    }

    /**
     * Returns an array of all jars to be searched for Modules and loaded.
     */
    private File[] getJars() {
        return ((MCJailedCore) getPlugin()).getActualConfig().getModuleJarsDir().listFiles(new JarFilter());
    }

    public class ModuleMeta {

        private String main;

    }

}
