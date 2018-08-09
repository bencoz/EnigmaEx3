package Alies;

import Commons.*;
import Machine.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Alies implements Runnable {
    private String aliesName;
    private boolean ready = false;
    private Integer taskSize;
    private Integer blockSize; //block of tasks
    private transient DecipheringStatus status;
    private transient boolean gameFinished = false;
    private transient String codeToDecipher;
    private transient EnigmaMachine machine; //copy of the machine
    private transient DecipherMission mission;
    private transient List<CandidateForDecoding> candidacies;
    private long decipheringStartTime;
    private Integer handledTasksAmount = 0;
    private transient BlockingQueue<AgentResponse> answersToDM_Queue;
    private transient ServerSocket agentServer;
    private transient SocketHandler socketHandler;
    private Integer portNumber;
    private transient BlockingQueue<AliesResponse> answersFromAlies_Queue;
    private Integer numOfIteretion = 0;

    //TODO: change? need to get en and dic according to the chosen game
    public Alies(String _name) {
        aliesName = _name;
        candidacies = new ArrayList<>();
        status = new DecipheringStatus();
        socketHandler = new SocketHandler();
        try {
            agentServer = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        portNumber = agentServer.getLocalPort(); //the only port
    }

    public String getAliesName() {
        return aliesName;
    }
    public void setNewGameDetails(EnigmaMachine em, BlockingQueue<AliesResponse> _answersFromAlies_Queue){
        machine = em;
        //excludeWords = em.getDecipher().getExcludeChars();
        answersFromAlies_Queue = _answersFromAlies_Queue;
        candidacies = new ArrayList<>();
        status = new DecipheringStatus();
    }

    @Override
    public void run(){
        if (machine.getSecret() == null){
            System.out.println("no secret !");
            return;
        }
        this.decipheringStartTime = System.currentTimeMillis();
        init(codeToDecipher, machine.getBattlefield().getLevel());
        activateAgents();
    }

    public void init(String _code, Factory.DifficultyLevel _difficulty){ //task size already Initialized
        mission = new DecipherMission(machine,_difficulty);
        mission.init(machine, taskSize);
        this.codeToDecipher = _code;
        answersToDM_Queue = new LinkedBlockingDeque<>();

        //calculate block size
        //TODO:what block size means?!
        Double tempSize = Math.log(mission.getSize());
        blockSize = tempSize.intValue();
    }

    private void activateAgents() {
        //TODO :: Work by number of agents
        while (true){
            Socket socket;
            try {
                socket = agentServer.accept();
                new Thread(() -> {
                    try {
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        String agentName = (String) ois.readObject();
                        socketHandler.put(agentName, socket, ois);
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
        Boolean done = false;
        while (!done) {
            giveAgentBlockOfTasks(agentName);
            //loopAgentDetails(agentName);
            getAgentResponses(agentName); //maybe just return response (instead of get and poll)
            AgentResponse response = answersToDM_Queue.poll();
            if (response != null) {
                handleAgentResponse(response);
                handledTasksAmount++;
            }
            updateAgentDetails(agentName);

            done = mission.isDone() || !status.checkIfToContinue();
            System.out.println("alies is done: " + done.toString());
            sendDecipherStatus(agentName);
        }
        status.stopDeciphering();
    }

    private void loopAgentDetails(String agentName) {
        Boolean done = false;
        AgentAliesSocket agentAliesSocket = socketHandler.get(agentName);
        try {
            while (!done){
                    String request = (String) agentAliesSocket.getOIS().readObject();
                    if (request.equals("end")){
                        done = true;
                        break;
                    }
                    AgentDetails details = (AgentDetails) agentAliesSocket.getOIS().readObject();
                    socketHandler.updateAgentDetails(agentName, details);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateAgentDetails(String agentName) {//TODO :: IMPLENMENT !!!

    }


    private void closeConnection(String agentName) { //TODO :: IMPLENMENT !!!
    }

    private void giveAgentBlockOfTasks(String agentName) {
        try {
            AgentAliesSocket agentAliesSocket = socketHandler.get(agentName);
            AgentTask task;
            for (int i = 0; i < taskSize; i++) {
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
                        agentAliesSocket.getOOS().writeObject(taskSize);
                        agentAliesSocket.getOOS().writeObject(machine.getDecipher().getDictionary());
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
        AliesResponse aliesResponse;
        for (CandidateForDecoding candidate: response.getCandidacies())
        {
            candidacies.add(candidate);
            aliesResponse = new AliesResponse(candidate.getDecoding(),aliesName);
            try {
                answersFromAlies_Queue.put(aliesResponse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendDecipherStatus(String agentName) {
        AgentAliesSocket agentAliesSocket = socketHandler.get(agentName);
        try{
            System.out.println("writing status...");
            agentAliesSocket.getOOS().writeObject(status);
            System.out.println("wrote status of iteration "+ numOfIteretion++);
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

    public List<CandidateForDecoding> getCandidacies() {
        return candidacies;
    }

    /*public void setAnswersQueue(BlockingQueue<AliesResponse> _answersToUboat_queue) {
        answerToUboat_Queue = _answersToUboat_queue;
    }*/

    public void stopDeciphering() {
        status.stopDeciphering();
    }

    /*public void setMachineCopy(EnigmaMachine _machineCopy) {
        machine = _machineCopy;
    }*/

    public int getPortNumber() {
        return portNumber;
    }

    public void setTaskSize(Integer taskSize) {
        this.taskSize = taskSize;
    }

    public void setAsReady() {
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public void resetGameDetails() {
        machine = null;
        answersFromAlies_Queue = null;
        candidacies = null;
        status = null;
    }

    public int getVersion() {
        return candidacies.size();
    }

    public int getNumOfAgents() {return socketHandler.getNumOfAgents(); }

    public Collection<AgentDetails> getAgentsDetails() {
        return socketHandler.getAgentsDetails();
    }

    public void setTarget(String target) {
        this.codeToDecipher = target;
    }
}
