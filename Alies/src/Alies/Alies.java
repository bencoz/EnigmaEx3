package Alies;

import Commons.*;
import Machine.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Alies {
    private String aliesName;
    private String codeToDecipher;
    private List<String> dictionary;
    private String excludeWords;
    private Integer numOfAgents;
    private Integer taskSize;
    private Integer maxNumOfAgents;
    private EnigmaMachine machine; //copy of the machine
    private DecipherMission mission;
    private Integer blockSize; //block of tasks
    private List<CandidateForDecoding> candidacies;
    private DecipheringStatus status;
    private long decipheringStartTime;
    private Integer handledTasksAmount = 0;
    private boolean gameFinished = false;
    private BlockingQueue<AgentResponse> answersToDM_Queue;
    private ServerSocket agentServer;
    private SocketHandler socketHandler;

    private BlockingQueue<AliesResponse> answerToUboat_Queue;

    //TODO: change? need to get en and dic according to the chosen game
    public Alies(String _name) {
        aliesName = _name;
    }

    public void setNewGameDetails(EnigmaMachine em, List<String> _dictionary, BlockingQueue<AliesResponse> _answersFromAlies_Queue){
        machine = em;
        dictionary = _dictionary;
        excludeWords = em.getDecipher().getExcludeChars();
        maxNumOfAgents = em.getDecipher().getMaxNumOfAgents();
        candidacies = new ArrayList<>();
        status = new DecipheringStatus();
        socketHandler = new SocketHandler();
        try {
            agentServer = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        answerToUboat_Queue = _answersFromAlies_Queue;
        System.out.println("listening on port: " + agentServer.getLocalPort());
    }


    public void start(){
        this.decipheringStartTime = System.currentTimeMillis();
        activateAgents();
        /*while( !gameFinished){
            try {
                AgentResponse response = answersToDM_Queue.take();
                checkresponse(response);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sleep(gameFinishe);
        killreamaingthreads();*/
    }

    public boolean init(String _code, DifficultyLevel _difficulty, Integer _taskSize, Integer _numOfAgents){
        mission = new DecipherMission(machine,_difficulty);
        mission.init(machine, _taskSize, _numOfAgents);
        numOfAgents = _numOfAgents;
        taskSize = _taskSize;
        if (mission.getSize() < _taskSize*numOfAgents)
            return false;
        this.codeToDecipher = _code;
        answersToDM_Queue = new ArrayBlockingQueue<>(numOfAgents);

        //calculate block size
        Double tempSize = Math.log(mission.getSize());
        blockSize = tempSize.intValue();
        return true;
    }

    private void activateAgents() {
        //TODO :: Work by number of agents
        for (int i = 0; i < numOfAgents; ++i) {
            Socket socket;
            try {
                socket = agentServer.accept();
                new Thread(() -> {
                    try {
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        String agentName = (String) ois.readObject();
                        socketHandler.put(agentName, socket);
                        initAgent(agentName);
                        runConnection(agentName);
                        closeConnection(agentName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void runConnection(String agentName) {
        boolean done = false;
        while (!done) {
            giveAgentBlockOfTasks(agentName);
            getAgentResponses(agentName); //maybe just return response (instead of get and poll)
            AgentResponse response = answersToDM_Queue.poll();
            if (response != null) {
                handleAgentResponse(response);
                handledTasksAmount++;
            }
            done = mission.isDone() || !status.checkIfToContinue();
            sendDecipherStatus(agentName);
        }
        status.stopDeciphering();
    }


    private void closeConnection(String agentName) { //TODO :: IMPLENMENT !!!
    }

    private void giveAgentBlockOfTasks(String agentName) {
        try {
            AgentAliesSocket agentAliesSocket = socketHandler.get(agentName);
            AgentTask task;
            for (int i = 0; i < blockSize; i++) {
                task = mission.getNextTask();
                if (task == null)
                    break;
                agentAliesSocket.getOOS().writeObject(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAgent(String agentName) {
        AgentAliesSocket agentAliesSocket = socketHandler.get(agentName);
        boolean finished = false;
        try {
            while (!finished) {
                String request = (String) agentAliesSocket.getOIS().readObject();
                switch (request) {
                    case "init":
                        agentAliesSocket.getOOS().writeObject(machine);
                        agentAliesSocket.getOOS().writeObject(codeToDecipher);
                        agentAliesSocket.getOOS().writeObject(blockSize);
                        agentAliesSocket.getOOS().writeObject(dictionary);
                        break;
                    case "initialized":
                        finished = true;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return;
    }


    private void handleAgentResponse(AgentResponse response) {
        //TODO: send the candidacies to uboat and STOP if needed
        //TODO: who need to do this? each tread or the main tread
        if (response.isEmpty())
            return;
        for (CandidateForDecoding candidate: response.getCandidacies())
        {
            candidacies.add(candidate);
        }
    }

    private void sendDecipherStatus(String agentName) {
        AgentAliesSocket agentAliesSocket = socketHandler.get(agentName);
        try{
            agentAliesSocket.getOOS().writeObject(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAgentResponses(String agentName) {
        AgentAliesSocket agentAliesSocket = socketHandler.get(agentName);
        try {
            AgentResponse response = (AgentResponse) agentAliesSocket.getOIS().readObject();
            answersToDM_Queue.add(response);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAnswersQueue(BlockingQueue<AliesResponse> _answersToUboat_queue) {
        answerToUboat_Queue = _answersToUboat_queue;
    }

    public void stopDeciphering() {
        status.stopDeciphering();
    }

    public void setMachineCopy(EnigmaMachine _machineCopy) {
        machine = _machineCopy;
    }
}
