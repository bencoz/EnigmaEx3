package GameManager;

import Alies.Alies;
import Alies.AliesResponse;
import Alies.DifficultyLevel;
import Machine.EnigmaMachine;
import Uboat.Uboat;
import Users.User;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Game {
    private Uboat managingUboat;
    private List<Alies> playingAlies ;
    private String winningAliesName = null;
    private Integer neededNumOfAlies;
    private Integer numOfAliesSigned = 0;
    private String battlefieldName;
    private BlockingQueue<AliesResponse> answersFromAlies_Queue;
    private GameStatus gameStatus;
    private Factory.DifficultyLevel difficultyLevel;

    //copy of the machine and code for the playing alies
    private EnigmaMachine machine;
    private List<String> dictionary;
    private String encryptedCode;

    //private machineCopy; // for the playing alies to clone

    public Game(String _battlefieldName, Integer _neededNumOfAlies, Factory.DifficultyLevel difficultyLevel){
        battlefieldName = _battlefieldName;
        neededNumOfAlies = _neededNumOfAlies;
        answersFromAlies_Queue = new ArrayBlockingQueue<>(neededNumOfAlies);
        playingAlies = new ArrayList<>();
        gameStatus = GameStatus.UNINITIALIZED;
    }



    public void addPlayingAlies(Alies _alies){
        playingAlies.add(_alies);
        numOfAliesSigned++;
        _alies.setNewGameDetails(machine.deepCopy(),dictionary, answersFromAlies_Queue);
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
        return machine.deepCopy();
    }

    public boolean isRightAnswer(String _answer){
        return managingUboat.isRightAnswer(battlefieldName, _answer);
    }

    public void setAsInit(){
        gameStatus = GameStatus.WAITING;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public void loadSettings(List<Integer> chosenRotorsID, List<Integer> chosenRotorsID1, Integer chosenReflectorID) {

    }

    public void setMachineAs(EnigmaMachine _machine) {
        machine = _machine;
    }
}
