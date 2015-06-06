import com.mcjailed.api.module.AbstractModule;
import com.mcjailed.api.module.Cmd;
import com.mcjailed.api.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClearChat extends AbstractModule<ClearChatConfig> {

    private ClearChatConfig config;

    @Override
    public String getName() {
        return "Clear Chat";
    }

    @Override
    public Class<ClearChatConfig> getConfigClass() {
        return ClearChatConfig.class;
    }

    @Override
    public void onEnable(ClearChatConfig config) {
        super.onEnable(config);

        this.config = config;

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

        getCommandRegistry().deregisterCommandHandler(this);
    }

    @Cmd(value = "clearchat", aliases = { "cc", "clearc", "chatclear", "chatc", "cchat", "cclear" })
    public boolean onClearChat(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("clearchat")) {
            for (int i = 0; i < config.getNumberOfLines(); i ++) {
                Bukkit.broadcastMessage("");
            }

            if (config.doBroadcast()) {
                Bukkit.broadcastMessage(StringUtils.format(config.getPrefix() + config.getCleared()));
            }
        } else {
            sender.sendMessage(StringUtils.format(config.getPrefix() + config.getNoPerm()));
        }

        return true;
    }

}
