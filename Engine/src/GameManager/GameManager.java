package GameManager;

import Alies.*;
import Enigma.*;
import Factory.DifficultyLevel;

import java.io.InputStream;
import java.util.*;
import Uboat.*;

public class GameManager {
    private Map<Integer, Game> games; //key will be battlefieldName
    private Map<String, Uboat> playingUboats;
    private Map<String, Alies> playingAlies;
    private String error;

    public GameManager(){
        games = new HashMap<>();
        playingUboats = new HashMap<>();
        playingAlies = new HashMap<>();
    }

    public Game createGame(String managingUboat_name, InputStream stream)
    {
        EnigmaManager manager;
        boolean userHaveGame = findUboatGame(managingUboat_name);
        if (userHaveGame){
            Game game = getGameByUboatName(managingUboat_name);
            manager = game.getEnigmaManger();
        } else {
            manager = new EnigmaManager();
        }
        boolean buildMachineOk = manager.createEnigmaMachineFromXMLInputStream(stream);
        if (!buildMachineOk){
            Game game = new Game();
        }
        String battlefieldName = enigmaManager.getBattlefield().getName();
        Integer neededNumOfAlies = enigmaManager.getBattlefield().getNumOfAllies();
        DifficultyLevel difficultyLevel = enigmaManager.getBattlefield().getLevel();

        Game newGame = new Game(battlefieldName, neededNumOfAlies, difficultyLevel);

        //search if managingUboat exist in the system and if not create him
        Uboat managingUboat = findOrCreatePlayingUboat(managingUboat_name);
        newGame.setManagerAs(managingUboat);
        managingUboat.addGame(newGame.getBattlefieldName());
    }
    public void initGame(){

    }
    public void addAliesToGame(String _newAliesName, String _battlefieldName){
        Alies newAlies = findOrCreatePlayingAlies(_newAliesName);
        Game game = findGame(_battlefieldName);
        game.addPlayingAlies(newAlies);
    }

    private Alies findOrCreatePlayingAlies(String _aliesName) {
        Alies resAlies;
        if(playingAlies.containsKey(_aliesName)) {
            resAlies = playingAlies.get(_aliesName);
        }
        else{
            resAlies = new Alies(_aliesName);
            playingAlies.put(_aliesName, resAlies);
        }
        return resAlies;
    }



    /*public void setGameCode(String _managingUboat_name, Integer _gameID){
        Uboat managingUboat = findPlayingUboat(_managingUboat_name);
        Game currGame = findGame(_gameID);
        managingUboat.setGameCode(_gameID, currGame.getMachineCopy());//TODO: get config and code
    }*/

    private Game findGame(String _battlefieldName) {
        Game resGame = null;
        if(games.containsKey(_battlefieldName)){
            resGame = games.get(_battlefieldName);
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

    public boolean isGameExist(String battlefieldName)
            //compare battlefieldName
    {
        boolean res = false;
        if(games.containsKey(battlefieldName))
            res = true;
        return res;
    }

    public void loadGameSettings(String _battleName, EnigmaManager enigmaManager, List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, Integer chosenReflectorID) {
        Game game = findGame(_battleName);
        enigmaManager.setMachineConfig(chosenRotorsID,chosenRotorsLoc,chosenReflectorID);
        game.setMachineAs(enigmaManager.getMachine());
    }

    public Collection<Game> getGames(){
        return games.values();
    }

    public Game getGame(String gameName) {
        return games.get(gameName);
    }
}
