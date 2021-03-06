package Alies;

import Commons.AgentAliesSocket;
import Commons.AgentDetails;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketHandler {
    private Map<String, AgentAliesSocket> agentSockets;
    private Map<String, AgentDetails> agentDetails;
    SocketHandler(){
        agentSockets = new HashMap<>();
        agentDetails = new HashMap<>();
    }

    public void put(String key, Socket value, ObjectInputStream ois){
        agentSockets.put(key, new AgentAliesSocket(value, ois));
        agentDetails.put(key, new AgentDetails(key,0,0));
    }

    public AgentAliesSocket get(String agentName) {
        return agentSockets.get(agentName);
    }

    public Integer getNumOfAgents(){
        return agentSockets.size();
    }
    public void updateAgentDetails(String agentName, AgentDetails details){
        agentDetails.put(agentName, details);
    }
    public Collection<AgentDetails> getAgentsDetails(){
        return agentDetails.values();
    }

    public void removeAgent(String agentName) {
        agentSockets.remove(agentName);
        agentDetails.remove(agentName);
    }

    public void removeAllAgents(){
        for (String agentName : agentSockets.keySet()){
            agentSockets.remove(agentName);
            agentDetails.remove(agentName);
        }
    }
}
