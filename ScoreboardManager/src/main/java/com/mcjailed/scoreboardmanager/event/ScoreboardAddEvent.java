package com.mcjailed.scoreboardmanager.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardAddEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Scoreboard scoreboard;
    private Player player;

    public ScoreboardAddEvent(Scoreboard scoreboard, Player player) {
        this.scoreboard = scoreboard;
        this.player = player;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
