package com.mcjailed.rankup.scoreboard;

import com.mcjailed.api.MCJailed;
import com.mcjailed.rankup.Rankup;
import com.mcjailed.rankup.config.RankupConfig.Rank;
import com.mcjailed.rankup.player.RankupPlayer;
import com.mcjailed.scoreboardmanager.IScoreboardHandler;
import com.mcjailed.scoreboardmanager.ScoreboardValue;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankupScoreboardHandler implements IScoreboardHandler {

    private Rankup rankup;

    public RankupScoreboardHandler(Rankup rankup) {
        this.rankup = rankup;
    }

    @Override
    public List<ScoreboardValue> getValues(Player player) {
        List<ScoreboardValue> values = new ArrayList<ScoreboardValue>();
        RankupPlayer p = new RankupPlayer(player);

        if (rankup.isEnabled()) {
            if (MCJailed.getVaultRegistry().isEconomyLoaded()) {
                String key = "&6Money&8:";
                String value = "&8> &e$" + p.getMCJailedPlayer().getBalanceFormatted();

                values.add(new ScoreboardValue(key, value));
            }

            if (p.getCurrentRank() != null) {
                Rank rank = p.getNextRank();
                String key = "&aYou are the";
                String value = "&ahighest rank!";

                if (rank != null) {
                    key = "&6Needed&8:";
                    value = "&8> &e$" + p.getMCJailedPlayer().getBalanceFormatted(rank.getPrice());
                    values.add(new ScoreboardValue(key, value));

                    key = "&6Next Rank&8:";
                    value = "&8> &e" + rank.getGroup();
                }

                values.add(new ScoreboardValue(key, value));
            }

            if (p.getNextRank() != null) {
                int percent = (int) (Math.min( p.getMCJailedPlayer().getBalance() / p.getNextRank().getPrice(), 1) * 100);
                String key = "&6Progress&8:";
                String value = "&8> &e" + (percent >= 100 ? "/rankup" : (percent + "%"));

                values.add(new ScoreboardValue(key, value));
            }

            if (p.getCurrentRank() != null) {
                String key = "&6Rank&8:";
                String value = "&8> " + p.getCurrentRank().getGroup();

                values.add(new ScoreboardValue(key, value));
            }

            String key = "&6Online&8:";
            String value = "&8> &e" + rankup.getPlugin().getServer().getOnlinePlayers().size();

            values.add(new ScoreboardValue(key, value));
        }

        return values;
    }

}
