package Alies;

import Commons.AgentAliesSocket;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketHandler {
    private Map<String, AgentAliesSocket> agentSockets;

    SocketHandler(){
        agentSockets = new HashMap<>();
    }

    public void put(String key, Socket value){

        agentSockets.put(key,new AgentAliesSocket(value));
    }

    public AgentAliesSocket get(String agentName) {
        return agentSockets.get(agentName);
    }
}
