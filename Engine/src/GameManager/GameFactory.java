package GameManager;

public class GameFactory {
    private static int nextGameID = 0;

    public static Game createGame(){
        Game resGame = new Game(nextGameID);

        return resGame;
    }
}
