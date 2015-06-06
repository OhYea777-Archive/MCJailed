package com.mcjailed.scoreboardmanager;

import org.bukkit.entity.Player;

import java.util.List;

public interface IScoreboardHandler {

    public List<ScoreboardValue> getValues(Player player);

}
