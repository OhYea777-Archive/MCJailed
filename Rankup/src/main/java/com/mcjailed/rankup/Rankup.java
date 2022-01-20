package com.mcjailed.rankup;

import com.mcjailed.api.MCJailed;
import com.mcjailed.api.module.AbstractModule;
import com.mcjailed.api.module.Cmd;
import com.mcjailed.rankup.config.RankupConfig;
import com.mcjailed.rankup.player.RankupPlayer;
import com.mcjailed.rankup.scoreboard.RankupScoreboardHandler;
import com.mcjailed.scoreboardmanager.ScoreboardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rankup extends AbstractModule<RankupConfig> {

    private RankupConfig config;
    private RankupScoreboardHandler handler;

    @Override
    public String getName() {
        return "Rankup";
    }

    @Override
    public Class<RankupConfig> getConfigClass() {
        return RankupConfig.class;
    }

    @Override
    public void onEnable(RankupConfig config) {
        this.config = config;
        this.handler = new RankupScoreboardHandler(this);

        if (MCJailed.getModules().moduleExists(ScoreboardManager.class)) {
            MCJailed.getModules().getModule(ScoreboardManager.class).registerScoreboardHandler(handler);
        }

        getCommandRegistry().registerCommandHandler(this);
    }

    @Override
    public void onReload() {
        super.onReload();
        config = reloadConfig();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (MCJailed.getModules().moduleExists(ScoreboardManager.class)) {
            MCJailed.getModules().getModule(ScoreboardManager.class).unregisterScoreboardHandler(handler);
        }

        getCommandRegistry().deregisterCommandHandler(this);
    }

    @Cmd(value = "rankup", aliases = { "rup" })
    public boolean onRankup(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) new RankupPlayer((Player) sender).rankup();

        return true;
    }

    @Cmd(value = "ranks", aliases = { })
    public boolean onRanskupMenu(CommandSender sender, Command command, String label, String[] args) {
        // if (sender instanceof Player && new RankupPlayer((Player) sender).getCurrentRank() != null) RankupMenu.createFromPlayer(new RankupPlayer((Player) sender));

        return true;
    }

    public RankupConfig getConfig() {
        return config;
    }

}
