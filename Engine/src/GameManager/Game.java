package GameManager;

import Alies.Alies;
import Alies.AliesResponse;
import Alies.DifficultyLevel;
import Enigma.EnigmaManager;
import Machine.EnigmaMachine;
import Uboat.Uboat;
import Users.User;

import java.io.InputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Game implements Serializable{
    private Uboat managingUboat;
    private transient List<Alies> playingAlies ;
    private transient String winningAliesName = null;
    private Integer neededNumOfAlies;
    private Integer numOfAliesSigned = 0;
    private String battlefieldName;
    private transient BlockingQueue<AliesResponse> answersFromAlies_Queue;
    private GameStatus gameStatus;
    private Factory.DifficultyLevel difficultyLevel;

    //copy of the machine and code for the playing alies
    private transient EnigmaManager enigmaManager;
    private transient List<String> dictionary;

    private String encryptedCode;

    //private machineCopy; // for the playing alies to clone

    public Game(String _battlefieldName, Integer _neededNumOfAlies, Factory.DifficultyLevel _difficultyLevel){
        battlefieldName = _battlefieldName;
        neededNumOfAlies = _neededNumOfAlies;
        answersFromAlies_Queue = new ArrayBlockingQueue<>(neededNumOfAlies);
        playingAlies = new ArrayList<>();
        gameStatus = GameStatus.UNINITIALIZED;
        difficultyLevel = _difficultyLevel;
    }



    public void addPlayingAlies(Alies _alies){
        playingAlies.add(_alies);
        numOfAliesSigned++;
        _alies.setNewGameDetails(getMachineCopy(),dictionary, answersFromAlies_Queue);
        if(numOfAliesSigned == neededNumOfAlies)
        {
            gameStatus = GameStatus.ACTIVE;
            runGame();
        }
    }

    private void runGame()
    {
        boolean done = false;
        activateAlies();
        while (!done) {
            try {
                AliesResponse response = answersFromAlies_Queue.take();
                if(isRightAnswer(response.getAnswer())){
                    winningAliesName = response.getAliesName();
                    stopGame();
                    done = true;
                }
                else {
                    done = (gameStatus == GameStatus.DONE);  //game Turned off
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopGame();
    }

    private void activateAlies() {
        for (Alies alies: playingAlies ) {
            alies.start();
        }
    }

    private void stopGame() {
        for (Alies alies: playingAlies ) {
            alies.stopDeciphering();
        }
    }


    public void setManagerAs(Uboat _managingUboat)
    {
        managingUboat = _managingUboat;
    }

    public EnigmaMachine getMachineCopy() {
        return enigmaManager.getMachine().deepCopy();
    }

    public boolean isRightAnswer(String _answer){
        return managingUboat.isRightAnswer(_answer);
    }

    public void setAsInit(){
        gameStatus = GameStatus.WAITING;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public void setEncryptedCode(String encryptedCode) {
        this.encryptedCode = encryptedCode;
    }

    public void setEnigmaManager(EnigmaManager _enigmaManager) {
        enigmaManager = _enigmaManager;
    }

    public Set<String> getAliesNames() {
        Set<String> res = new HashSet<>();
        for (Alies alies : playingAlies){
            res.add(alies.getName());
        }
        return res;
    }

    public EnigmaManager getEnigmaManager() {
        return enigmaManager;
    }

    public Alies getAlies(String aliesName) {
        Alies res = null;
        for (Alies alies : playingAlies){
            if (alies.getName() == aliesName){
                res = alies;
                break;
            }
        }
        return res;
    }
}
