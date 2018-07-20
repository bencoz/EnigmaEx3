package Uboat;

//import Machine.EnigmaMachine;

import java.io.*;
import java.util.*;

public class Uboat {
    private String uboatName;
    //private EnigmaManager enigmaManager;
    private Map<String, String> managedGamesAnswers;//key is gameName, value is the OriginalCode

    public Uboat(String _uboatName){
        uboatName = _uboatName;
        managedGamesAnswers = new HashMap<>();
    }

    public void addGame(String _battlefieldName)
    {
        managedGamesAnswers.put(_battlefieldName,"Uninitialized");
    }

    /*public void setGameCode(Integer gameID, EnigmaMachine machineCopy) {
    }*/

    public boolean isRightAnswer(String _battlefieldName, String _answer) {
        String answer = managedGamesAnswers.get(_battlefieldName);
        if(answer.equals( _answer))
            return true;
        else
            return false;
    }
}
