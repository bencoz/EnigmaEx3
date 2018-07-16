package GameManager;

import java.net.ServerSocket;
import java.util.List;

public class Game {
    private Integer gameId;
    private List<Integer> playingAliesId;
    private ServerSocket aliesServer; //all playing alies will get the socket address
    //private machineCopy; // for the playing alies to clone

    public Game(Integer _gameID){
        gameId = _gameID;
    }
}
