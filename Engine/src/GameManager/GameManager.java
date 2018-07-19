package GameManager;

import Alies.Alies;
import Users.*;

import java.util.*;
import Uboat.*;

public class GameManager {
    private Map<Integer,Game> games; //key will be gameID
    private Map<String, Uboat> playingUboats;
    private Map<String, Alies> playingAlies;

    public GameManager(){
        games = new HashMap<>();
        playingUboats = new HashMap<>();
        playingAlies = new HashMap<>();
    }

    public void createGame(String managingUboat_name, String xmlPath)
            //the managingUboat is the user how send the request to open the game
    {
        Game newGame = GameFactory.createGameFromXml(xmlPath);
        if(isGameExist(newGame)) {
            //need to return error
        }
        else {
            //search if managingUboat exist in the system and if not create him
            Uboat managingUboat = findOrCreatePlayingUboat(managingUboat_name);
            newGame.setManagerAs(managingUboat);
            managingUboat.addGame(newGame.getID());
        }
    }

    public void setGameCode(String _managingUboat_name, Integer _gameID){
        Uboat managingUboat = findPlayingUboat(_managingUboat_name);
        Game currGame = findGame(_gameID);
        managingUboat.setGameCode(_gameID, currGame.getMachineCopy());//TODO: get config and code
    }

    private Game findGame(Integer _gameID) {
        Game resGame = null;
        if(games.containsKey(_gameID)){
            resGame = games.get(_gameID);
        }
        return resGame;
    }

    public Uboat findOrCreatePlayingUboat(String _uboatName)
    {
        Uboat resUboat;
        if(playingUboats.containsKey(_uboatName)) {
            resUboat = playingUboats.get(_uboatName);
        }
        else{
            resUboat = new Uboat(_uboatName);
            playingUboats.put(_uboatName, resUboat);
        }
        return resUboat;
    }

    public Uboat findPlayingUboat(String _uboatName)
    {
        //return null if not found
        Uboat resUboat = null;
        if(playingUboats.containsKey(_uboatName)) {
            resUboat = playingUboats.get(_uboatName);
        }
        return resUboat;
    }

    public boolean isGameExist(Game _game)
            //compare battlefieldName
    {
        return false;
    }

}
