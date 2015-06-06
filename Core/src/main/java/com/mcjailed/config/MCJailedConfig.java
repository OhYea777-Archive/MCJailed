package com.mcjailed.config;

import com.mcjailed.MCJailedCore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MCJailedConfig {

    private String prefix = "&8[&6MCJailed&8] &e";
    private String moduleEnable = "&aThe Module &8'&e{module}&8' &ais now enabled!";
    private String moduleAlreadyEnabled = "&cThe Module &8'&e{module}&8' &cis already enabled!";
    private String moduleNotFound = "&cThe Module &8'&e{module}&8' &cdoes not exist!";
    private String moduleDisabled = "&cThe Module &8'&e{module}&8' &cis now disabled!";
    private String moduleAlreadyDisabled = "&cThe Module &8'&e{module}&8' &cis already disabled!";
    private String moduleReloaded = "The Module &8'&e{module}&8' &ehas been reloaded!";
    private String invalidArgs = "&cInvalid arguments!";
    private String moduleFormat = "&6Modules &8(&ePage {page}&8/&e{maxpages}&8)&6:";
    private String bulletPoint = "&8-&6] ";

    private int modulesPerPage = 10;

    private String moduleClasses = "{dataFolder}/ModuleClasses";
    private String moduleJars = "{dataFolder}/ModuleJars";
    private List<String> defaultDisabledModules = new ArrayList<String>();

    public String getPrefix() {
        return prefix;
    }

    public String getModuleEnable() {
        return moduleEnable;
    }

    public String getModuleAlreadyEnabled() {
        return moduleAlreadyEnabled;
    }

    public String getModuleNotFound() {
        return moduleNotFound;
    }

    public String getModuleDisabled() {
        return moduleDisabled;
    }

    public String getModuleAlreadyDisabled() {
        return moduleAlreadyDisabled;
    }

    public String getModuleReloaded() {
        return moduleReloaded;
    }

    public String getInvalidArgs() {
        return invalidArgs;
    }

    public String getModuleFormat() {
        return moduleFormat;
    }

    public String getBulletPoint() {
        return bulletPoint;
    }

    public int getModulesPerPage() {
        return modulesPerPage;
    }

    public File getModuleClassesDir() {
        File file = new File(moduleClasses.replace("{dataFolder}", getPlugin().getDataFolder().getAbsolutePath()));

        if (!file.exists()) file.mkdirs();

        return file;
    }

    public File getModuleJarsDir() {
        File file = new File(moduleJars.replace("{dataFolder}", getPlugin().getDataFolder().getAbsolutePath()));

        if (!file.exists()) file.mkdirs();

        return file;
    }

    public List<String> getDefaultDisabledModules() {
        List<String> disabledModules = new ArrayList<String>();

        for (String module : defaultDisabledModules) disabledModules.add(module.toLowerCase());

        return disabledModules;
    }

    private MCJailedCore getPlugin() {
        return MCJailedCore.getInstance();
    }

}
