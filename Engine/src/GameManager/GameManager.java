package GameManager;

import Alies.*;
import Enigma.*;
import Factory.DifficultyLevel;

import java.util.*;
import Uboat.*;

public class GameManager {
    private Map<String, Game> games; //key will be battlefieldName
    private Map<String, Uboat> playingUboats;
    private Map<String, Alies> playingAlies;
    private String error;

    public GameManager(){
        games = new HashMap<>();
        playingUboats = new HashMap<>();
        playingAlies = new HashMap<>();

        /*Game g1 = new Game("game1",2,DifficultyLevel.Easy);
        Uboat uboat1 = new Uboat("Eden");
        g1.setManagerAs(uboat1);

        Game g2 = new Game("game2",5,DifficultyLevel.Hard);
        Uboat uboat2 = new Uboat("Ben");
        g2.setManagerAs(uboat2);

        games.put("game1",g1);
        games.put("game2",g2);*/
    }

    public void createGame(String managingUboat_name, EnigmaManager enigmaManager)
    {
        String battlefieldName = enigmaManager.getBattlefield().getName();
        Integer neededNumOfAlies = enigmaManager.getBattlefield().getNumOfAllies();
        DifficultyLevel difficultyLevel = enigmaManager.getBattlefield().getLevel();

        Game newGame = new Game(battlefieldName, neededNumOfAlies, difficultyLevel);

        //search if managingUboat exist in the system and if not create him
        Uboat managingUboat = findPlayingUboat(managingUboat_name);
        newGame.setManagerAs(managingUboat);
        newGame.setEnigmaManager(enigmaManager);
        managingUboat.setGame(newGame.getBattlefieldName());
        games.put(battlefieldName, newGame);
    }

    public void addAliesToGame(String _newAliesName, String _battlefieldName){
        Alies newAlies = findPlayingAlies(_newAliesName);
        Game game = findGame(_battlefieldName);
        game.addPlayingAlies(newAlies);
    }

    private Alies findPlayingAlies(String _aliesName) {
        Alies resAlies = null;
        if(playingAlies.containsKey(_aliesName)) {
            resAlies = playingAlies.get(_aliesName);
        }
        return resAlies;
    }


    public String setGameCode(String _managingUboat_name, String code){
        Uboat managingUboat = findPlayingUboat(_managingUboat_name);
        managingUboat.setCode(code);
        Game currGame = findGame(managingUboat.getGameName());
        String encodedCode = currGame.getEnigmaManager().process(code);
        currGame.setEncryptedCode(encodedCode);
        return encodedCode;
    }

    private Game findGame(String _battlefieldName) {
        Game resGame = null;
        if(games.containsKey(_battlefieldName)){
            resGame = games.get(_battlefieldName);
        }
        return resGame;
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

    public void loadGameSettings(String _battleName, List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, Integer chosenReflectorID) {
        Game game = findGame(_battleName);
        EnigmaManager em = game.getEnigmaManager();
        em.setMachineConfig(chosenRotorsID, chosenRotorsLoc, chosenReflectorID);
    }

    public Collection<Game> getGames(){
        return games.values();
    }

    public Game getGame(String gameName) {
        return games.get(gameName);
    }

    public EnigmaManager createNewEnigmaManager() {
        return new EnigmaManager();
    }

    public EnigmaManager getBattlefieldEnigmaManager(String battleName) {
        EnigmaManager battleEnigmaManager = null;
        Game game = findGame(battleName);
        if (game != null){
            battleEnigmaManager = game.getEnigmaManager();
        }
        return  battleEnigmaManager;
    }

    public int getAliesPort(String usernameFromSession) {
        Alies alies = playingAlies.get(usernameFromSession);
        return alies.getPortNumber();
    }

    public void addAlies(String userName){
        Alies alies = new Alies(userName);
        playingAlies.put(userName,alies);
    }

    public  void addUboat(String userName){
        Uboat uboat = new Uboat(userName);
        playingUboats.put(userName , uboat);
    }
}
