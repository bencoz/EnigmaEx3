package GameManager;

import Alies.Alies;
import Alies.AliesResponse;
import Enigma.EnigmaManager;
import Factory.DifficultyLevel;
import Machine.EnigmaMachine;
import Uboat.Uboat;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Game implements Runnable {
    private String battlefieldName;
    private Uboat managingUboat;
    private transient List<Alies> playingAlies ;
    private transient String winningAliesName = null;
    private Integer neededNumOfAlies;
    private Integer numOfAliesSigned = 0;
    private GameStatus gameStatus;
    private Factory.DifficultyLevel difficultyLevel;
    private transient EnigmaManager enigmaManager;
    private String encryptedCode = null;
    private transient BlockingQueue<AliesResponse> answersFromAlies_Queue;
    private List<AliesResponse> allCandidates;
    //private machineCopy; // for the playing alies to clone
    //private transient List<String> dictionary;

    public Game(String _battlefieldName, Integer _neededNumOfAlies, Factory.DifficultyLevel _difficultyLevel){
        battlefieldName = _battlefieldName;
        neededNumOfAlies = _neededNumOfAlies;
        difficultyLevel = _difficultyLevel;
        answersFromAlies_Queue = new LinkedBlockingDeque<AliesResponse>();
        allCandidates = new LinkedList<>();
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
        if(alies != null) {
            alies.setAsReady();
            boolean needToStart = areAllPlayersReady();
            if (needToStart) {
                gameStatus = GameStatus.ACTIVE;
            }
        }
    }

    private boolean areAllPlayersReady() {
        boolean res = true;
        if(!managingUboat.isReady())
            res = false;
        if (playingAlies.size() != neededNumOfAlies)
            res = false;
        for (Alies alies : playingAlies ) {
            if(!alies.isReady()){
                res = false;
            }
        }
        return res;
    }

    public void setUboatAsReady(String userName){
        if(managingUboat.getName() == userName) {
            managingUboat.setAsReady();
            gameStatus = GameStatus.WAITING;
            boolean needToStart = areAllPlayersReady();
            if (needToStart) {
                gameStatus = GameStatus.ACTIVE;
            }
        }
    }

    @Override
    public void run()
    {
        this.gameStatus = GameStatus.RUNNING;
        boolean done = false;
        activateAlies();
        while (!done) {
            try {
                AliesResponse response = answersFromAlies_Queue.take();
                allCandidates.add(response);
                if(isRightAnswer(response.getAnswer())){
                    winningAliesName = response.getAliesName();
                    done = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopGame();
    }

    private void activateAlies() {//TODO:add init
        for (Alies alies: playingAlies ) {
            alies.setTarget(this.encryptedCode);
            Thread thread = new Thread(alies);
            thread.setName(alies.getAliesName());
            thread.start();
        }
    }

    private void stopGame() {
        gameStatus = GameStatus.DONE;
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
            res.add(alies.getAliesName());
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
            if (alies.getAliesName() == aliesName){
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
    }

    public void removeAlies(Alies alies) {
        if(playingAlies.contains(alies)) {
            playingAlies.remove(alies);
            numOfAliesSigned--;
        }
    }

    public List<AliesResponse> getAllCandidates() {
        return allCandidates;
    }

    public int getVersion() {
        return allCandidates.size();
    }

    public String getTarget() {
        return encryptedCode;
    }

    public List<String> getAllReadyNames() {
        List<String> result = new LinkedList<>();
        if (managingUboat.isReady())
            result.add(managingUboat.getName());
        for (Alies alies : playingAlies){
            if (alies.isReady())
                result.add(alies.getAliesName());
        }
        return result;
    }

    public Integer getWantedNumOfAlies() {
        return neededNumOfAlies;
    }

    public boolean isRunnable() {
        if (gameStatus == GameStatus.ACTIVE) {
            //this.setName("game "+ this.battlefieldName);
            return true;
        } else {
            return false;
        }
    }

    public String getMakerName() {
        return managingUboat.getName();
    }

    public DifficultyLevel getLevel() {
        return difficultyLevel;
    }

    public Integer getNeededNumOfAlies() {
        return neededNumOfAlies;
    }

    public GameStatus getStatus() {
        return gameStatus;
    }

    public void giveAllAliesSecret() {
        for (Alies alies : playingAlies){
            alies.setMachineCopy(enigmaManager.getMachine().deepCopy());
        }
    }
}
