package GameManager;

import java.util.concurrent.BlockingQueue;

public class GameFactory {
    private static int nextGameID = 0;

    public static Game createGameFromXml(String xmlPath){
        //return false if failure
        Game resGame = null;
        //= new Game(nextGameID, _battlefieldName, _neededNumOfAlies, _answerToUboat_Queue);
        nextGameID++;
        return resGame;
    }
}
