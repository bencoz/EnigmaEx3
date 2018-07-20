package GameManager;

import Enigma.EnigmaManager;

import java.util.concurrent.BlockingQueue;

public class GameFactory {
    private static int nextGameID = 0;

    public static Game createGame(EnigmaManager enigmaManager){
        Game resGame = null;
        //= new Game( _battlefieldName, _neededNumOfAlies, _answerToUboat_Queue);
        nextGameID++;
        return resGame;
    }
}
