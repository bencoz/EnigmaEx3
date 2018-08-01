package Uboat;

//import Machine.EnigmaMachine;

import java.io.*;
import java.util.*;
import java.io.Serializable;

public class Uboat implements Serializable {
    private String uboatName;
    private String gameName;
    private String code;
    private boolean ready = false;

    public Uboat(String _uboatName){
        uboatName = _uboatName;
    }

    public void setGame(String _battlefieldName) {
        gameName = _battlefieldName;
    }

    public void setCode(String i_code){
        code = i_code;
    }

    public boolean isRightAnswer(String _answer) {
        if(code.equals( _answer))
            return true;
        else
            return false;
    }

    public String getGameName() {
        return gameName;
    }

    public boolean isReady() {
        return ready;
    }

    public String getName() {
        return uboatName;
    }

    public void setAsReady() {
        ready = true;
    }
}
