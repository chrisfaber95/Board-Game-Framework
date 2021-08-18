package Framework.Model;

public class PlayerModel {

    private String playerName;
    private String currentPage;

    public PlayerModel() {
        currentPage = "Login";
        playerName = "";
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
}
