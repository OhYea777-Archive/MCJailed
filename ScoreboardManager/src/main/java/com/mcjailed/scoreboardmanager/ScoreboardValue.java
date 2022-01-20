package com.mcjailed.scoreboardmanager;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class ScoreboardValue {

    private String key, value;

    public ScoreboardValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void onScoreboard(Objective objective, int from) {
        String key = format(getKey().substring(0, Math.min(getKey().length(), 16)));
        String value = format(getValue().substring(0, Math.min(getValue().length(), 16)));

        Score keyScore = objective.getScore(key);
        Score valueScore = objective.getScore(value);

        keyScore.setScore(from + 1);
        valueScore.setScore(from);
    }

}
