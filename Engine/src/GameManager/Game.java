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
    private String battlefieldName;
    private Uboat managingUboat;
    private transient List<Alies> playingAlies ;
    private transient String winningAliesName = null;
    private Integer neededNumOfAlies;
    private Integer numOfAliesSigned = 0;
    private Integer numOfAliesReady = 0;
    private GameStatus gameStatus;
    private Factory.DifficultyLevel difficultyLevel;

    private transient EnigmaManager enigmaManager;
    private String encryptedCode;
    private transient BlockingQueue<AliesResponse> answersFromAlies_Queue;

    //private machineCopy; // for the playing alies to clone
    //private transient List<String> dictionary;

    public Game(String _battlefieldName, Integer _neededNumOfAlies, Factory.DifficultyLevel _difficultyLevel){
        battlefieldName = _battlefieldName;
        neededNumOfAlies = _neededNumOfAlies;
        difficultyLevel = _difficultyLevel;
        answersFromAlies_Queue = new ArrayBlockingQueue<>(neededNumOfAlies);
        playingAlies = new ArrayList<>();
        gameStatus = GameStatus.UNINITIALIZED;
    }



    public void addPlayingAlies(Alies _alies){
        if(numOfAliesSigned != neededNumOfAlies)
        {
            playingAlies.add(_alies);
            numOfAliesSigned++;
            _alies.setNewGameDetails(getMachineCopy() , answersFromAlies_Queue);
        }
    }

    public void setAliesAsReady(String userName){
        Alies alies = getAliesByName(userName);
        alies.setAsReady();
        numOfAliesReady++;
        boolean needToStart = areAllPlayersReady();
        if(needToStart){
            gameStatus = GameStatus.ACTIVE;
            runGame();
        }
    }

    private boolean areAllPlayersReady() {
        if(managingUboat.isReady() == false)
            return false;
        for (Alies alies : playingAlies ) {
            if(alies.isReady() != false){
                return false;
            }
        }
        return true;
    }

    public void setUboatAsReady(String userName){
        if(managingUboat.getName() == userName) {
            managingUboat.setAsReady();
            gameStatus = GameStatus.WAITING;
            boolean needToStart = areAllPlayersReady();
            if (needToStart) {
                gameStatus = GameStatus.ACTIVE;
                runGame();
            }
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
                    gameStatus = GameStatus.DONE;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopGame();
    }

    private void activateAlies() {//TODO:add init
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

    public List<Alies> getAlies() {
        return  playingAlies;
    }

    public EnigmaManager getEnigmaManager() {
        return enigmaManager;
    }

    public Alies getAliesByName(String aliesName) {
        Alies res = null;
        for (Alies alies : playingAlies){
            if (alies.getName() == aliesName){
                res = alies;
                break;
            }
        }
        return res;
    }

    public void reset() {
        answersFromAlies_Queue = new ArrayBlockingQueue<>(neededNumOfAlies);
        playingAlies = new ArrayList<>(); //need to be empty..
        gameStatus = GameStatus.WAITING;
        winningAliesName = null;
        numOfAliesSigned = 0;
        numOfAliesReady = 0;
    }

    public void removeAlies(Alies alies) {
        if(playingAlies.contains(alies)) {
            playingAlies.remove(alies);
            numOfAliesSigned--;
        }
    }
}
