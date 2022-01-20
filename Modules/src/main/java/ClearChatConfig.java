public class ClearChatConfig {

    private String prefix = "&3[&bClearChat&3] &7";
    private String cleared = "The chat has been cleared!";
    private String noPerm = "&cYou do not have permission to clear the chat!";

    private int numberOfLines = 200;
    private boolean broadcast = true;

    public String getPrefix() {
        return prefix;
    }

    public String getCleared() {
        return cleared;
    }

    public String getNoPerm() {
        return noPerm;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public boolean doBroadcast() {
        return broadcast;
    }

}
