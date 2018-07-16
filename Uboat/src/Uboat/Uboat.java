package Uboat;

import Enigma.*;
import GameManager.Game;

import java.util.ArrayList;
import java.util.List;

public class Uboat {
    private String uboatName;
    private EnigmaManager enigmaManager;
    private List<Integer> managedGamesID;

    public Uboat(String _uboatName){
        uboatName = _uboatName;
        enigmaManager = new EnigmaManager();
        managedGamesID = new ArrayList<>();
    }

    //save the new game ID to the managedGames array
    public void createGame(String xmlPath){
        Integer gameID = 0;
        if(enigmaManager.createEnigmaMachineFromXMLFile(xmlPath))//success
        {
            //need to set Configuration - rand or choose?
            String code = "decide where do we get the code from";
            String precessedCode = enigmaManager.process(code);
        }
        else
        {
            String error = enigmaManager.getErrorInMachineBuilding();
        }
        managedGamesID.add(gameID);
    }

}
