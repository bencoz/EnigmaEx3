package Uboat;

import Machine.EnigmaMachine;

import java.io.*;
import java.util.*;

public class Uboat {
    private String uboatName;
    //private EnigmaManager enigmaManager;
    private Map<Integer, String> managedGamesAnswers;//key is gameID, value is the originalCode

    public Uboat(String _uboatName){
        uboatName = _uboatName;
        //enigmaManager = new EnigmaManager();
        managedGamesAnswers = new HashMap<>();
    }

    /*save the new game ID to the managedGames array
    public void createGame(String xmlPath){
        Integer gameID = 0;
        if(enigmaManager.createEnigmaMachineFromXMLFile(xmlPath))//success
        {
            //need to choose Configuration and code
            String code = "";
            String precessedCode = enigmaManager.process(code);
            int neededNumOfAlies = 1;
            String battlefieldName = "";
            //Game newGame = GameFactory.createGame(battlefieldName, neededNumOfAlies);
            managedGames.put(battlefieldName,code);
        }
        else
        {
            String error = enigmaManager.getErrorInMachineBuilding();
        }
    }*/

    public void addGame(Integer gameId)
    {
        managedGamesAnswers.put(gameId,"Uninitialized");
    }

    public void setGameCode(Integer gameID, EnigmaMachine machineCopy) {
    }

    public boolean isRightAnswer(Integer _gameID, String _answer) {
        String answer = managedGamesAnswers.get(_gameID);
        if(answer.equals( _answer))
            return true;
        else
            return false;
    }
}
