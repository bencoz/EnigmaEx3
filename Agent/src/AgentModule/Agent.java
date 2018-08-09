package AgentModule;

import Commons.*;
import Machine.EnigmaMachine;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Agent {
    private String agentName;
    private EnigmaMachine machine; //copy of the machine
    private String code;
    private List<AgentTask> tasks ;
    private AgentTask currentTask;
    private Integer currentTaskInd = 0;
    private AgentResponse response;
    private DecipheringStatus DMstatus;
    private Integer handledOptionsAmount = 0;
    private Integer tasksAmount; //num of tasks that received each time ( == BlockSize)
    private BlockingQueue<AgentTask> tasksFromDM_Queue;
    private BlockingQueue<AgentResponse> answersToDM_Queue;
    private List<String> dictionary; //copy of the dictionary
    private Socket MyClient;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Integer numOfCandidates = 0;
    public Agent(String[] addr){
        try {
            System.out.println("trying to connect with Alies...");
            MyClient = new Socket(addr[0], Integer.parseInt(addr[1]));
            System.out.println("connected successfully");
            //ois = new ObjectInputStream(MyClient.getInputStream());
            oos = new ObjectOutputStream(MyClient.getOutputStream());
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String[] ip;
        if (args.length < 1 || args.length >= 2){
            System.out.println("Please try again with only ip:port");
            return;
        } else {
            ip = getInput(args[0]);
            if (ip == null)
                return;
        }
        Agent agent = new Agent(ip);
        System.out.println("Agent working with " + ip[0] + ":" + ip[1]);
        while (aliesAgentConnectionIsAlive()){
            agent.initAgent();
            agent.run();
        }
        try {
            agent.ois.close();
            agent.oos.close();
            agent.MyClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean aliesAgentConnectionIsAlive() {
        return true; //TODO:: WORK GOOD
    }

    public void setName(String agentName) {
        this.agentName = agentName;
    }

    private void initAgent() {
        String init = "init";
        String initialized = "initialized";
        System.out.println("Starting init...");
        this.agentName = ManagementFactory.getRuntimeMXBean().getName();
        try {
            oos.writeObject(agentName);
            oos.flush();
            oos.writeObject(init);
            ois = new ObjectInputStream(MyClient.getInputStream());
            machine = (EnigmaMachine)ois.readObject();
            System.out.println("got machine...");
            code = (String) ois.readObject();
            System.out.println("got code...");
            tasksAmount = (Integer) ois.readObject();
            System.out.println("got task size...");
            dictionary = (List<String>) ois.readObject();
            System.out.println("got dictionary...");
            oos.writeObject(initialized);
            System.out.println("Initialized !");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.tasksFromDM_Queue = new ArrayBlockingQueue<>(tasksAmount);
        this.setName(agentName);
        this.response = new AgentResponse(agentName);
    }

    private static String[] getInput(String input) {
        String[] res = new String[2];
        Integer port = null;
        if (input.indexOf(':') > -1) { // <-- does it contain ":"?
            String[] arr = input.split(":");
            res[0] = arr[0];
            try {
                port = Integer.parseInt(arr[1]);
                res[1] = arr[1];
            } catch (NumberFormatException e) {
                System.out.println("Please try again port must be a number");
                return  null;
                //e.printStackTrace();
            }
        } else {
            res = null;
        }
        return res;
    }

    //work on block of task that got from DM, solves each one, update the agent response (add the Candidacies For Decoding to it)
    private void doTasks() throws InterruptedException {
        try {
            for (int i = 0; i < tasks.size(); i++) {
                this.currentTask = tasks.get(i);
                currentTaskInd = i;
                doCurrentTask();
                oos.writeObject("details");
                oos.writeObject(makeAgentDetails());
            }

            oos.writeObject("end");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //work on current task update the agent response (add the Candidacies For Decoding to it)
    private void doCurrentTask(){
        int firstSize = currentTask.getLength();
        for(int i=0; i < firstSize; i++) {
            machine.initFromSecret(currentTask.getSecret());
            String decoding = machine.process(code);
            handledOptionsAmount++;

            if(isCandidaciesForDecoding(decoding)) {
                numOfCandidates++;
                CandidateForDecoding candidate = new CandidateForDecoding(decoding, currentTask.getSecret(), agentName);
                response.addDecoding(candidate);
            }
            if(currentTask.hasNext()){
                if (!currentTask.moveToNextCode(machine)) // moveToNextCode returns false when no more codes configurations left.
                    break;
            }
            else
                break;
        }
    }

    //gets code decoding and return true if all words in the dictionary and false otherwise
    private boolean isCandidaciesForDecoding(String decoding){
        boolean found;
        String[] words = removeExcludeCharsFromString(decoding, machine.getDecipher().getExcludeChars()).split(" ");
        for (String word : words){
            found = false;
            for (int i = 0; i < dictionary.size() && !found; i++) {
                String permittedWord = dictionary.get(i);
                if (permittedWord.equals(word)) {
                    found = true;
                }
            }
            if (!found)
                return false;
        }
        return true;
    }

    private String removeExcludeCharsFromString(String words, String excludeChars) {
        StringBuilder sb = new StringBuilder(words);
        for(int i = 0; i < sb.length(); i++)
        {
            Character temp = sb.charAt(i);
            if(excludeChars.contains(temp.toString()))
            {
                sb.deleteCharAt(sb.indexOf(temp.toString()));
            }
        }
        return sb.toString();
    }

    public BlockingQueue<AgentTask> getTasksQueue()
    {
        return tasksFromDM_Queue;
    }

    //agent wait and listen to pipe until he gets new mission from DM and start to work
    public void run() {
        System.out.println("Starting to run...");
        Boolean done = false;
        try {
            while (!done) {
                AgentTask task;
                tasks = new ArrayList<>();
                for (int i = 0; i < tasksAmount; i++) {
                    //TODO:: get tasks from socket
                    try {
                        System.out.println("taking task...");
                        task = (AgentTask) ois.readObject();
                        System.out.println("OK!...");
                        if (task == null){
                            break;
                        }
                        tasks.add(task);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Starting to do Tasks...");
                doTasks();
                //TODO:: send dm response through socket
                try {
                    oos.writeObject(response);
                    System.out.println("response was written...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reset();
                System.out.println("Agent did reset");
                //TODO:: pull dm status
                done = !pullDMStatus();
                System.out.println("Agent is stopping: "+ done.toString());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //TODO:: HANDLE STUFF AFTER INTERRUPT
            return;
        }
    }

    private AgentDetails makeAgentDetails() {
        return new AgentDetails(this.agentName, this.numOfCandidates, tasks.size() - currentTaskInd);
    }

    private boolean pullDMStatus() {
        try {
            DMstatus = (DecipheringStatus) ois.readObject();
            System.out.println(DMstatus.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DMstatus.checkIfToContinue();
    }

    private void reset()
    {
        tasks = null;
        currentTask = null;
        currentTaskInd = 0;
        response = new AgentResponse(this.agentName);
    }

    public AgentTask getCurrentTask() {
        return currentTask;
    }

    public String getDecipheringStatus(){
        StringBuilder sb = new StringBuilder();
        sb.append("Agent Name:").append(agentName).append("\n");
        if(tasks == null){
            sb.append("didn't get tasks yet").append("\n");
        }
        else {
            int numOfRemainTasks = tasks.size() - currentTaskInd;
            sb.append("Tasks remaining: ").append(numOfRemainTasks);
        }
        return sb.toString();
    }

    public Integer getHandledOptionsAmount() {
        return handledOptionsAmount;
    }

}
