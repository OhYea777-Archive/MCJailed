package com.mcjailed.scoreboardmanager;

import com.mcjailed.api.module.AbstractModule;
import com.mcjailed.api.util.StringUtils;
import com.mcjailed.scoreboardmanager.event.ScoreboardAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class ScoreboardManager extends AbstractModule<ScoreboardConfig> implements Runnable {

    private ScoreboardConfig config;
    private int scheduleId = -1;
    private List<IScoreboardHandler> handlers = new ArrayList<IScoreboardHandler>();
    private Map<UUID, Integer> exemptedPlayers = new HashMap<UUID, Integer>();

    @Override
    public String getName() {
        return "Scoreboard Manager";
    }

    @Override
    public Class<ScoreboardConfig> getConfigClass() {
        return ScoreboardConfig.class;
    }

    @Override
    public void onEnable(ScoreboardConfig config) {
        this.config = config;
        scheduleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), this, getRefreshRate(), getRefreshRate());
    }

    @Override
    public void onReload() {
        super.onReload();
        config = reloadConfig();
    }

    @Override
    public void onDisable() {
        getPlugin().getLogger().info("Module: [" + getName() + "] has been disabled");

        if (scheduleId != -1) getPlugin().getServer().getScheduler().cancelTask(scheduleId);

        for (Player player : getPlugin().getServer().getOnlinePlayers())
            player.setScoreboard(getPlugin().getServer().getScoreboardManager().getMainScoreboard());
    }

    private String getTitle() {
        return StringUtils.format(config.getTitle());
    }

    private String getSubtitle() {
        return StringUtils.format(config.getSubtitle());
    }

    private int getRefreshRate() {
        return config.getRefreshRate();
    }

    public List<IScoreboardHandler> getHandlers() {
        return handlers;
    }

    public void registerScoreboardHandler(IScoreboardHandler handler) {
        handlers.add(handler);
    }

    public void unregisterScoreboardHandler(IScoreboardHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public void run() {
        for (Player player : getPlugin().getServer().getOnlinePlayers()) {
            createScoreboard(player);
        }
    }

    public void unExemptPlayer(Player player) {
        if (exemptedPlayers.containsKey(player.getUniqueId())) {
            getPlugin().getServer().getScheduler().cancelTask(exemptedPlayers.remove(player.getUniqueId()));
        }
    }

    public void exemptPlayer(final Player player, long duration) {
        exemptedPlayers.put(player.getUniqueId(), getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                unExemptPlayer(player);
            }
        }, duration * 20));

        player.setScoreboard(getPlugin().getServer().getScoreboardManager().getMainScoreboard());
    }

    public void createScoreboard(Player player) {
        if (!exemptedPlayers.containsKey(player.getUniqueId())) {
            Scoreboard scoreboard = getPlugin().getServer().getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("stats", "dummy");
            int id = 1;

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(getTitle());

            for (IScoreboardHandler handler : getHandlers()) {
                for (ScoreboardValue value : handler.getValues(player)) {
                    value.onScoreboard(objective, id);

                    id += 2;
                }
            }

            Score subtitle = objective.getScore(getSubtitle());
            subtitle.setScore(id);

            player.setScoreboard(scoreboard);
            getPlugin().getServer().getPluginManager().callEvent(new ScoreboardAddEvent(scoreboard, player));
        }
    }

}
